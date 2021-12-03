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
import com.bsq.aee.ui.account.register.RegisterActivity;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import timber.log.Timber;


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
    private final ActivityResultLauncher<Intent> mLoginWithGoogleIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

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
        }
        mGoogleSignInClient.signOut();

    });

    @Override
    protected void onResume() {
        super.onResume();
        doCheckOnStartUp();
    }

    private void doCheckOnStartUp() {
        viewModel.doProfile(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                Timber.d(error);
                viewBinding.splashScreen.setVisibility(View.GONE);
            }

            @Override
            public void doSuccess() {
                navigateToMainActivity();
            }

            @Override
            public void doFail() {
                viewBinding.splashScreen.setVisibility(View.GONE);
            }
        });
    }

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
                            viewModel.mFirebaseToken = getTokenResult.getToken();
                            viewModel.mFirebaseUID = task.getResult().getUser().getUid();
                            viewModel.mGmail = task.getResult().getUser().getEmail();
                            viewModel.mAvatarPath = Objects.requireNonNull(task.getResult().getUser().getPhotoUrl()).toString();
                            if (viewModel.loginWithGoogle){
                                loginWithGoogle();
                                viewModel.loginWithGoogle = false;
                            } else {
                                checkRegister();
                            }
                        });

                    } else {
//                         If sign in fails, display a message to the user.
                        viewModel.showErrorMessage(getString(R.string.login_error));
                        viewModel.hideLoading();
                    }
                });
    }
    private void loginWithGoogle(){
        viewModel.doLoginWithGoogle(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                // TODO login success
                viewModel.showSuccessMessage("Login success");
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.wrong_email_password));
            }
        });
    }
    private void checkRegister() {
        viewModel.checkRegister(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                // TODO navigate to create account activity
                navigateToRegisterActivity();
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }
        });
    }

    public static final String LOGIN_EMAIl = "LOGIN_EMAIl";
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final String LOGIN_UID = "LOGIN_UID";
    private void navigateToRegisterActivity() {
        viewModel.hideLoading();
        Intent it = new Intent(this, RegisterActivity.class);
        it.putExtra(LOGIN_EMAIl,viewModel.mGmail);
        it.putExtra(LOGIN_TOKEN,viewModel.mFirebaseToken);
        it.putExtra(LOGIN_UID,viewModel.mFirebaseUID);
        startActivity(it);
    }
    private void navigateToMainActivity() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
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
        viewModel.showLoading();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mLoginWithGoogleIntent.launch(signInIntent);
    }

    private void doLoginWithGoogle() {
        viewModel.loginWithGoogle = true;
        doRegister();
    }

    private void doLogin() {
        viewModel.showLoading();
        viewModel.doLogin(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                viewModel.showSuccessMessage("Login success");
                viewModel.hideLoading();
                navigateToMainActivity();
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.wrong_email_password));
            }
        });
    }

    private void navigateToMain(){
        Intent it = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
        finish();
    }
}


