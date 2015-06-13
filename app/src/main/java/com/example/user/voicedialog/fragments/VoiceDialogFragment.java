package com.example.user.voicedialog.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.user.voicedialog.R;
import com.example.user.voicedialog.SettingsActivity;
import com.example.user.voicedialog.adapters.QuestionsAdapter;
import com.example.user.voicedialog.adapters.SqlDatabaseHelper;
import com.example.user.voicedialog.errors.ErrorMessages;
import com.example.user.voicedialog.helper.Helper;
import com.example.user.voicedialog.mappers.QuestionMapper;
import com.example.user.voicedialog.models.Question;
import com.example.user.voicedialog.sender.SenderRequest;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.PreferenceChangeListener;

/**
 * Created by Bunis on 07.05.2015.
 */
public class VoiceDialogFragment extends Fragment implements
    RecognitionListener,TextToSpeech.OnInitListener{
    private Button startListenQuestion;
    private Response.Listener<String> responseQuestion;
    private Response.ErrorListener errorListener;
    private SenderRequest sw;
    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private EditText editTextInputQuestion;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private Button sendQuestionButton;
    private String questionText;
    private String answerText;
    private List<Question> listQuestions;
    private ListView questionsListView;
    private QuestionsAdapter questionsAdapter;
    private SharedPreferences prefs;
    public static String URL;
    private PreferenceChangeListener mPreferenceListener = null;
    private TextToSpeech textToSpeech;
    private Boolean startAnswerPlay;
    private Boolean startRequest;
    private Boolean saveHistory;
    private SqlDatabaseHelper sqlDatabaseHelper;
    private static final int REQUEST_CODE = 1234;
    private Dialog dialogListener ;
    private ProgressBar progressBarAudio1;
    private ProgressBar progressBarAudio2;
    private ProgressBar progressBarAudio3;
    private ProgressBar progressBarAudio4;
    private ImageView imageViewAudio;
    private ImageView serverAnimation;
    private ImageView microphoneImage;
    public VoiceDialogFragment() {
    }

    public static VoiceDialogFragment newInstance()
    {
        VoiceDialogFragment voiceDialogFragment = new VoiceDialogFragment();
        return  voiceDialogFragment;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.voice_dialog_layout,container,false);
        textToSpeech= new TextToSpeech(getActivity(),this);
        returnedText = (TextView) rootView.findViewById(R.id.textView1);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) rootView.findViewById(R.id.toggleButton1);

        startListenQuestion = (Button) rootView.findViewById(R.id.activity_main_button_start_lisneting);
        editTextInputQuestion = (EditText) rootView.findViewById(R.id.activity_main_edittext_question);

        sendQuestionButton = (Button) rootView.findViewById(R.id.activity_main_button_send_question);
        sendQuestionButton.setVisibility(View.INVISIBLE);
        toggleButton.setVisibility(View.INVISIBLE);
        editTextInputQuestion.setVisibility(View.INVISIBLE);
        questionsListView = (ListView) rootView.findViewById(R.id.acivity_main_listvie_question_history);
        android.support.v7.widget.Toolbar actionbar = (android.support.v7.widget.Toolbar)rootView.findViewById(R.id.toolbarVoiceDialogMain);
        actionbar.inflateMenu(R.menu.menu_voice_dialog);
        Helper.InitialHelper(getActivity());
        dialogListener = new Dialog(getActivity());

        dialogListener.setContentView(R.layout.audio_dialog_layout);
        dialogListener.setTitle("Говорите...");
//        progressBarAudio4 =(ProgressBar) dialogListener.findViewById(R.id.progressBarAudio4);
//        progressBarAudio4.setProgressDrawable(getResources().getDrawable(R.drawable.verticalprogressbar2));
//        progressBarAudio4.setIndeterminate(false);
//        progressBarAudio4.setMax(10);
//        progressBarAudio4.setProgress(0);
//
//        progressBarAudio2 =(ProgressBar) dialogListener.findViewById(R.id.progressBarAudio1);
//        progressBarAudio2.setProgressDrawable(getResources().getDrawable(R.drawable.verticalprogressbar2));
//        progressBarAudio2.setIndeterminate(false);
//        progressBarAudio2.setMax(10);
//        progressBarAudio2.setProgress(0);
//
//        progressBarAudio3 =(ProgressBar) dialogListener.findViewById(R.id.progressBarAudio2);
//        progressBarAudio3.setProgressDrawable(getResources().getDrawable(R.drawable.verticalprogressbar2));
//        progressBarAudio3.setIndeterminate(false);
//        progressBarAudio3.setMax(10);
//        progressBarAudio3.setProgress(0);
//
//        progressBarAudio1 =(ProgressBar) dialogListener.findViewById(R.id.progressBarAudio3);
//        progressBarAudio1.setProgressDrawable(getResources().getDrawable(R.drawable.verticalprogressbar2));
//        progressBarAudio1.setIndeterminate(false);
//        progressBarAudio1.setMax(10);
//        progressBarAudio1.setProgress(0);
        imageViewAudio = (ImageView) dialogListener.findViewById(R.id.imageViewAudio2);
        serverAnimation = (ImageView)dialogListener.findViewById(R.id.imageViewAudioAnimation);
        microphoneImage = (ImageView)dialogListener.findViewById(R.id.imageViewAudio);
        AnimationDrawable frameAnimation = (AnimationDrawable)serverAnimation.getDrawable();
        frameAnimation.setCallback(serverAnimation);
        frameAnimation.setVisible(true, true);
        actionbar.setOnMenuItemClickListener( new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        questionText = "";
        answerText = "";
        URL="";

        listQuestions= new ArrayList<Question>();
        sqlDatabaseHelper = new SqlDatabaseHelper(getActivity());
        //listQuestions.addAll(sqlDatabaseHelper.getAllQuestion());
        questionsAdapter= new QuestionsAdapter(getActivity(),listQuestions);
        questionsListView.setAdapter(questionsAdapter);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        URL = prefs.getString(SettingsActivity.KEY_PREF_SYNC_ADDRESS, "");
        startAnswerPlay= prefs.getBoolean(SettingsActivity.CHECK_BOX_PREF_START_ANSWER_PLAY,false);
        startRequest= prefs.getBoolean(SettingsActivity.CHECK_BOX_PREF_VOICE_START_REQUEST,false);
        saveHistory=prefs.getBoolean(SettingsActivity.SAVE_HISTORY_PREFERENCE,false);

        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        if (key.equals(SettingsActivity.KEY_PREF_SYNC_ADDRESS)) {
                            URL= prefs.getString(SettingsActivity.KEY_PREF_SYNC_ADDRESS,"");
                        }
                        if(key.equals(SettingsActivity.CHECK_BOX_PREF_START_ANSWER_PLAY))
                        {
                          startAnswerPlay=  prefs.getBoolean(SettingsActivity.CHECK_BOX_PREF_START_ANSWER_PLAY,false);
                        }
                        if(key.equals(SettingsActivity.CHECK_BOX_PREF_VOICE_START_REQUEST))
                        {
                           startRequest=  prefs.getBoolean(SettingsActivity.CHECK_BOX_PREF_VOICE_START_REQUEST,false);
                        }
                        if(key.equals(SettingsActivity.SAVE_HISTORY_PREFERENCE))
                        {
                          saveHistory= prefs.getBoolean(SettingsActivity.SAVE_HISTORY_PREFERENCE,false);
                        }
                    }
                };
        prefs.registerOnSharedPreferenceChangeListener(listener);
        responseQuestion = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                answerText = response;
                try {
                    response = new String(response.getBytes("windows-1251"), Charset.forName("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //returnedText.setText(Html.fromHtml(answerText));
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                Question question =QuestionMapper.MapStringToQuestion(response,questionText);
               // Question question = new Question(questionText, answerText);
               // sqlDatabaseHelper.addItem(question);
                listQuestions.add(question);
                //listQuestions.add(new Question(questionText, answerText));
                ((BaseAdapter)questionsListView.getAdapter()).notifyDataSetChanged();

                if(startAnswerPlay==true)
                    textToSpeech.speak(question.getAnswerText(), TextToSpeech.QUEUE_FLUSH, null);
                editTextInputQuestion.setText("");
                dialogListener.dismiss();
                imageViewAudio.setVisibility(View.VISIBLE);
                microphoneImage.setVisibility(View.VISIBLE);
                serverAnimation.setVisibility(View.GONE);
                dialogListener.setTitle("Говорите...");
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                returnedText.setText(volleyError.getMessage());
                dialogListener.dismiss();
                imageViewAudio.setVisibility(View.VISIBLE);
                microphoneImage.setVisibility(View.VISIBLE);
                serverAnimation.setVisibility(View.GONE);
                dialogListener.setTitle("Говорите...");
            }
        };
        sw = new SenderRequest(getActivity(), responseQuestion, errorListener);
        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.ACTION_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
                //startVoiceRecognitionActivity();
            }
        });
        startListenQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech.startListening(recognizerIntent);
                dialogListener.show();
                //progressBar.setIndeterminate(true);
            }
        });
        sendQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                questionText = editTextInputQuestion.getText().toString();
                params.put("Q", questionText);
                params.put("D", "false");
                params.put("S", "true");
                params.put("server", "");
                sw.sendRequest(params, "http://" + URL + "/analyzer.php");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
            }
        });
        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textToSpeech.speak(Html.fromHtml(listQuestions.get(position).getAnswerText()).toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        return  rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_voice_dialog, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = ErrorMessages.getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
        dialogListener.dismiss();
    }
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        dialogListener.setTitle("Запрос на сервер...");
        final ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        microphoneImage.setVisibility(View.GONE);
        imageViewAudio.setVisibility(View.GONE);
        serverAnimation.setVisibility(View.VISIBLE);
        if(startRequest==true)
        {
            Map<String, String> params = new HashMap<String, String>();
            questionText = editTextInputQuestion.getText().toString();
            questionText=matches.get(0);
            params.put("Q",questionText);
            params.put("D", "false");
            params.put("S", "true");
            params.put("server", "");
            sw.sendRequest(params, "http://" + URL + "/analyzer.php");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_recognize_result);

        ListView lv = (ListView ) dialog.findViewById(R.id.recognize_result_dialog_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,matches);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questionText=matches.get(position);
                editTextInputQuestion.setText(questionText);
                dialog.dismiss();
                if(startRequest==true)
                {
                    Map<String, String> params = new HashMap<String, String>();
                    questionText = editTextInputQuestion.getText().toString();
                    params.put("Q", questionText);
                    params.put("D", "false");
                    params.put("S", "true");
                    params.put("server", "");
                    sw.sendRequest(params, "http://" + URL + "/analyzer.php");
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                }

            }
        });

        dialog.setCancelable(true);
        dialog.setTitle("Выберите верный вариант");
        //dialog.show();

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
//        progressBarAudio1.setProgress((int) rmsdB);
//        progressBarAudio2.setProgress((int) rmsdB);
//        progressBarAudio3.setProgress((int) rmsdB);
//        progressBarAudio4.setProgress((int) rmsdB);
        imageViewAudio.setScaleX(rmsdB/5);
        imageViewAudio.setScaleY(rmsdB/5);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale locale = new Locale("ru");

            int result = textToSpeech.setLanguage(locale);
            //int result = mTTS.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Извините, этот язык не поддерживается");
            } else {

            }

        } else {
            Log.e("TTS", "Ошибка!");
        }
    }
}
