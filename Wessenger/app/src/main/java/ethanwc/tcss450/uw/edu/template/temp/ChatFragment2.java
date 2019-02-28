package ethanwc.tcss450.uw.edu.template.temp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment2.OnChatFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link ChatFragment2#} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SELECT_IMAGE = 1;
    private static final int SELECT_VIDEO = 2;
    private static final int REQUEST_TAKE_PHOTO = 1111;
    private static final int TAKEN_PHOTO_UPLOAD = 444;
    private static final int REQUEST_VIDEO_CAPTURE = 555;
    private EditText mMessageInputEditText;
    private String currentPhotoPath;
    private String mEmail;
    private String mJwToken;
    private String mSendUrl;
    private String mChatID;
    ArrayList<ChatModel> list;
    private OnChatFragmentInteraction mListener;
    LinearLayout mediaBar;

    public ChatFragment2() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {

            Uri selectedVideo = data.getData();
            MediaManager.get()
                    .upload(selectedVideo)
                    .unsigned("u48dpnqx")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Toast.makeText(getActivity(), "Upload Started...", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {

                            Toast.makeText(getActivity(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();

                            String publicId = resultData.get("public_id").toString();

                            String firstImgUrl = MediaManager.get().url().transformation(new Transformation().startOffset("12")
                                    .border("5px_solid_black").border("5px_solid_black")).resourceType("video")
                                    .generate(publicId + ".jpg");
//                            Picasso.get().load(firstImgUrl).into(img1);

                            //todo: handle this

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                            Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_SHORT).show();
                            Log.v("ERROR!!", " VIDEO " + error.getDescription());
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }

                    }).dispatch();
        } else if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {

            Uri media = data.getData();

            if (isImageFile(Objects.requireNonNull(media).getPath())) uploadPhoto(media);
            else uploadVideo(media);
        }

        else if (requestCode == TAKEN_PHOTO_UPLOAD && resultCode == RESULT_OK) galleryAddPic();

        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            uploadVideo(videoUri);

        }
        else Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "ethanwc.tcss450.uw.edu.template.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKEN_PHOTO_UPLOAD);
            }
        }
    }

    private void dispatchTakeVideoIntent(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void pickFromGallery(View view) {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setType("image/* video/*");
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(GalleryIntent, "select media"), SELECT_IMAGE);
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Log.e("PHOTOURI", contentUri.toString());
        uploadPhoto(contentUri);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void uploadPhoto(Uri uri) {
        MediaManager.get()
                .upload(uri)
                .unsigned("u48dpnqx")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(getActivity(), "Upload Started...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Toast.makeText(getActivity(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();


                        String publicId = resultData.get("public_id").toString();

                        String firstImgUrl = MediaManager.get().url().transformation(new Transformation().startOffset("12")
                                .border("5px_solid_black").border("5px_solid_black")).resourceType("video")
                                .generate(publicId + ".jpg");
//                        Picasso.get().load(firstImgUrl).into(img1);

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_SHORT).show();
                        Log.v("ERROR!!", " IMAGE: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }

                }).dispatch();
    }

    private void uploadVideo(Uri uri) {
        MediaManager.get()
                .upload(uri)
                .option("resource_type", "video")
                .unsigned("u48dpnqx")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(getActivity(), "Upload Started...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Toast.makeText(getActivity(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();


                        String publicId = resultData.get("public_id").toString();

                        String firstImgUrl = MediaManager.get().url().transformation(new Transformation().startOffset("12")
                                .border("5px_solid_black").border("5px_solid_black")).resourceType("video")
                                .generate(publicId + ".jpg");
//                        Picasso.get().load(firstImgUrl).into(img1);

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_SHORT).show();
                        Log.v("ERROR!!", " IMAGE: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }

                }).dispatch();
    }

    @Override
    public void onStart() {
        super.onStart();

        //Store url in variable.
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();

        mediaBar = (LinearLayout) getView().findViewById(R.id.menubar_chat);
        mediaBar.setVisibility(View.GONE);

        getView().findViewById(R.id.button_chatbox_plus).setOnClickListener(this::toggleMediaMenu);
        getView().findViewById(R.id.button_chat_takeImage).setOnClickListener(this::dispatchTakePictureIntent);
        getView().findViewById(R.id.button_chat_takeVideo).setOnClickListener(this::dispatchTakeVideoIntent);
        getView().findViewById(R.id.button_chat_uploadMedia).setOnClickListener(this::pickFromGallery);

        //Store arguments in variables.



    }

    public void toggleMediaMenu(View view) {
        if (mediaBar.getVisibility() == View.GONE) mediaBar.setVisibility(View.VISIBLE);
        else mediaBar.setVisibility(View.GONE);
    }

    public void handleNewImage(String string) {
//        Picasso.get().load(firstImgUrl).into(img1);
    }

    public void finalizeChat() {
        mMessageInputEditText = getView().findViewById(R.id.edit_chat_message_input);
        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, true);
        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaManager.init(getActivity());

        if (getArguments() != null) {
            mEmail = getArguments().getString("email_token_123");
            mJwToken = getArguments().getString("jwt_token");
            mChatID = getArguments().getString("chat_id");
        }
        list = new ArrayList();

        setChatHistory();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_fragment2, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onChatFragmentInteraction(uri);
            //if image...open it?
        }
    }

    /**
     * Helper method used to handle sending a message.
     * @param theButton Button that was clicked to send message.
     */
    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("email", mEmail);
            messageJson.put("message", msg);
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Send message to web service.
        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error -> Log.e("ERROR", error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    private void setChatHistory() {
        String getAll = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_getAll))
                .build()
                .toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(getAll, messageJson)
                .onPostExecute(this::getAllHistory)
                .onCancelled(error -> Log.e("ERROR", error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    /**
     * Helper method used to clear the input field.
     * @param result JSON object returned from the web service.
     */
    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("success") && res.getBoolean("success")) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");
                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //output the chat history result
    private void getAllHistory(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("messages")) {
                JSONArray chatHistoryArray = res.getJSONArray("messages");
                //Log.e("history: ", "  " + res.get("messages"));
                for (int i = chatHistoryArray.length() -1; i >= 0 ; i--) {
                    Log.e("MESSAGEIS" ," " + chatHistoryArray.getString(i));
                    int data = chatHistoryArray.getJSONObject(i).getString("email").equals(mEmail) ? 1 : 0;
                    list.add(new ChatModel(ChatModel.TEXT_TYPE, chatHistoryArray.getJSONObject(i).getString("messages"), data));
                }
                finalizeChat();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatFragmentInteraction) {
            mListener = (OnChatFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChatFragmentInteraction {
        // TODO: Update argument type and name
        void onChatFragmentInteraction(Uri uri);
    }
}
