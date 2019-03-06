package com.example.android.tobefocused.ui.googleTasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskLists;
import com.example.android.tobefocused.data.network.RetrofitClient;
import com.example.android.tobefocused.databinding.ActivityGoogleTasksBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.tasks.model.TaskList;

import java.util.List;

public class GoogleTasksActivity extends AppCompatActivity {
    private ActivityGoogleTasksBinding binding;
    private static final String TASKS_SCOPE = "https://www.googleapis.com/auth/tasks";
    private static final int RC_SIGN_IN = 9001;
    // private static final String CLIENT_ID = "940478920092-3ijg55rh85esi5blqu85n2gbj29kkoc1.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleTasksAdapter googleTasksAdapter = new GoogleTasksAdapter();
    private List<TaskList> mTaskList;
    private TaskLists mTaskLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_google_tasks);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(TASKS_SCOPE))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        binding.disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });

    }

    private void getLists() {
        RetrofitClient.getTasksService().getTaskLists().enqueue(new Callback<TaskLists>() {
            @Override
            public void onResponse(Call<TaskLists> call, Response<TaskLists> response) {
                mTaskList = response.body().getItems();
                googleTasksAdapter.submitList(mTaskList);
            }

            @Override
            public void onFailure(Call<TaskLists> call, Throwable t) {
                Log.e("Call Error", t.getMessage(), t);
            }
        });
        setupRecView();
    }

    private void setupRecView() {
        binding.googleTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.googleTasksRecyclerView.setAdapter(googleTasksAdapter);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGNIN ERROR", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.statusTextView.setText(R.string.signed_out);
            binding.signOutAndDisconnect.setVisibility(View.GONE);
            binding.googleTasksRecyclerView.setVisibility(View.GONE);
        } else {
            binding.signInButton.setVisibility(View.GONE);
            binding.statusTextView.setText(account.getDisplayName());
            binding.signOutAndDisconnect.setVisibility(View.VISIBLE);
            binding.googleTasksRecyclerView.setVisibility(View.VISIBLE);
            getLists();
        }

    }
}
