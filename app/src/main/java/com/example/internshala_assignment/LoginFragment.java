package com.example.internshala_assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    ImageButton btnGoogleIcon;
    ProgressBar progressbar;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        create_google_request();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize_variables(view);
        initialize_onClicks();
        verify_user();
    }

    private void initialize_variables(View view) {
        mAuth = FirebaseAuth.getInstance();
        progressbar = view.findViewById(R.id.progressbar);
        btnGoogleIcon = view.findViewById(R.id.btnGoogleIcon);
    }

    private void initialize_onClicks() {
        btnGoogleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts google sign-in
                progressbar.setVisibility(View.VISIBLE);
                signIn();
            }
        });
    }

    // all methods below are related to google-sign-in
    private void verify_user() {
        // auto-login
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Fragment fragment = new DashboardFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }

    private void signIn() {
        Toast.makeText(getActivity(), "Google Sign-in", Toast.LENGTH_SHORT).show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void create_google_request() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Toast.makeText(getActivity(), "Loading Dashboard", Toast.LENGTH_SHORT).show();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // sign-in success so navigate to dashboard
                            Fragment fragment = new DashboardFragment();
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                            progressbar.setVisibility(View.GONE);
                        } else {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Sorry auth failed.", Toast.LENGTH_SHORT).show();// If sign in fails, display a message to the user.
                        }
                    }
                });
    }
}