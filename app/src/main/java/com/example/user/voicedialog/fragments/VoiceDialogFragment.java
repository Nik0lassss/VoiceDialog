package com.example.user.voicedialog.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.example.user.voicedialog.models.Question;
import com.example.user.voicedialog.sender.SenderRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    private String URL;
    private PreferenceChangeListener mPreferenceListener = null;
    private TextToSpeech textToSpeech;
    private Boolean startAnswerPlay;
    private Boolean startRequest;
    private Boolean saveHistory;
    private SqlDatabaseHelper sqlDatabaseHelper;


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
        editTextInputQuestion = (EditText) rootView.findViewById(R.id.activity_main_edittext_question);
        sendQuestionButton = (Button) rootView.findViewById(R.id.activity_main_button_send_question);
        questionsListView = (ListView) rootView.findViewById(R.id.acivity_main_listvie_question_history);
        android.support.v7.widget.Toolbar actionbar = (android.support.v7.widget.Toolbar)rootView.findViewById(R.id.toolbarVoiceDialogMain);
        actionbar.inflateMenu(R.menu.menu_voice_dialog);

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
        listQuestions.addAll(sqlDatabaseHelper.getAllQuestion());
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
                //returnedText.setText(Html.fromHtml(answerText));
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                Question question = new Question(questionText, answerText);
                sqlDatabaseHelper.addItem(question);
                listQuestions.add(new Question(questionText, answerText));
                ((BaseAdapter)questionsListView.getAdapter()).notifyDataSetChanged();

                if(startAnswerPlay==true)
                    textToSpeech.speak(Html.fromHtml(answerText).toString(), TextToSpeech.QUEUE_FLUSH, null);
                editTextInputQuestion.setText("");
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                returnedText.setText(volleyError.getMessage());
            }
        };
        sw = new SenderRequest(getActivity(), responseQuestion, errorListener);
        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
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
        final ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

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
        dialog.show();

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
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
