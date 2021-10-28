package com.bsq.aee.ui.account.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityLoginBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final ActivityResultLauncher<Intent> loginWithGoogleIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == Activity.RESULT_OK) {
            // There are no request codes
            Intent data = result.getData();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if (account.getIdToken() == null) {

                        viewModel.hideLoading();
                        viewModel.showErrorMessage(getString(R.string.login_fail));
                    } else {
                        firebaseAuthWithGoogle(account.getIdToken(), account.getEmail());
                    }
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.login_fail));
            }
        } else {
            viewModel.hideLoading();
        }
        mGoogleSignInClient.signOut();

    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        // login with google initialization
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void firebaseAuthWithGoogle(String idToken, String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        task.getResult().getUser().getIdToken(true).addOnSuccessListener(getTokenResult -> {
//                            viewModel.checkRegister(
//                                    getTokenResult.getToken(),
//                                    task.getResult().getUser().getUid(),
//                                    new LoginCallback() {
//                                        @Override
//                                        public void doError(Throwable error) {
//                                            error.printStackTrace();
//                                            LogService.e(error);
//                                        }
//
//                                        @Override
//                                        public void doSuccess() {
//                                            token = getTokenResult.getToken();
//                                            uid = task.getResult().getUser().getUid();
//                                            navigationToCreateAccount(mEmail);
//                                        }
//
//                                        @Override
//                                        public void badRequest(List<ErrorForm> errors) {
//                                            Timber.d(errors.toString());
//                                        }
//                                    },GOOGLE
//                            );
                        });

                    } else {
//                         If sign in fails, display a message to the user.
                        viewModel.showErrorMessage(getString(R.string.login_error));
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.login_with_google_btn:
                doLoginWithGoogle();
                break;
            case R.id.register_btn:
                doRegister();
                break;
            default:
                break;
        }

    }

    private void doRegister() {
    }

    private void doLoginWithGoogle() {
    }

    private void doLogin() {
        viewModel.doLogin(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.showErrorMessage(getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                // TODO login success
                viewModel.showSuccessMessage("Login success");
            }

            @Override
            public void doFail() {
                viewModel.showErrorMessage(getString(R.string.wrong_email_password));
            }
        });
    }
}


