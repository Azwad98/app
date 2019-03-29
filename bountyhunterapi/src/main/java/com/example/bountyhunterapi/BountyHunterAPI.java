package com.example.bountyhunterapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BountyHunterAPI {
    private Context context;
    private SharedPreferences preferences;
    private RetrofitServices services;


    public BountyHunterAPI(Context context) {
        this.context = context;
        services = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);
    }

    public void registerUser(String fistName, String lastName, String username, String email, String password, final registerCallBack callBack) {
        Call<Void> call = services.registerUser(fistName, lastName, username, email, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    callBack.registrationSuccess(true);
                    Toast.makeText(context, "Your account was successfully registered", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        JSONObject errorObj2 = new JSONObject(errorObj.getString("error"));
                        JSONArray errorArray = new JSONArray(errorObj2.getJSONArray("errors").toString());
                        JSONObject errorMessage = new JSONObject(errorArray.getJSONObject(0).toString());
                        if (errorMessage.getString("message").equals("username must be unique")) {
                            Toast.makeText(context, "Sorry that username has already been taken\nPlease enter a new one", Toast.LENGTH_LONG).show();
                        } else if (errorMessage.getString("message").equals("email must be unique")) {
                            Toast.makeText(context, "Sorry there is an account already linked to that email address\nPlease enter a different email address", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginUser(String username, String password, final FoundUserCallBack callBack) {
        Call<Token> call = services.loginUser(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    preferences.edit().putString("TOKEN", "Bearer " + response.body().getToken()).apply();
                    getLoggedInUser(preferences.getString("TOKEN", null), callBack);

                } else if (response.code() == 401) {
                    Toast.makeText(context, "The password you entered was incorrect", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "The username you entered was not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLoggedInUser(String token, final FoundUserCallBack callBack) {
        Call<User> call = services.getLoggedInUser(token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    callBack.onUserReturned(response.body());
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find the user account with the specified username and password \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getUser(UUID userID, final FoundUserCallBack callBack) {
        String token = preferences.getString("TOKEN", null);
        Call<User> call = services.getUser(token, userID);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    callBack.onUserReturned(response.body());
                    Toast.makeText(context, "User account was found", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find the user account with the specified id \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateUser(UUID userID, User updateUser, final FoundUserCallBack callBack) {
        String token = preferences.getString("TOKEN", null);
        Call<User> call = services.updateUser(token, userID, updateUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    callBack.onUserReturned(response.body());
                    Toast.makeText(context, "User account information was successfully updated", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to update the user information\n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not update the account information as the user was not found\n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void deleteUser(UUID userID, String password) {

        String token = preferences.getString("TOKEN", null);
        Call<Void> call = services.deleteUser(token, userID, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Toast.makeText(context, "Your account was successfully deleted", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to delete the account \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not delete the user as the user was not found\n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void searchUser(String username, final SearchUsersCallBack callBack) {
        String token = preferences.getString("TOKEN", null);
        Call<UserList> call = services.searchUser(token, username);

        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.code() == 200) {
                    callBack.onUsersFound(response.body().getUsers());
                    Toast.makeText(context, "User accounts with that username were found", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find any user accounts with the specified username \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void resetPasswordRequest(String email) {
        Call<Void> call = services.resetPasswordRequest(email);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "An email has been sent with the link to reset your password", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "There is no user that links to the email entered \n Please enter the email the email you used to register your account", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to send the email to reset your password\n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void resetPassoword(String token, String newPassword) {
        Call<Void> call = services.resetPassword(token, newPassword);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Your password was successfully reset", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not the user account to change the password for\nPlease try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "The link you used to has expired\nPlease complete the forget password form again to be sent a new link", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to reset your password\nPlease try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getFugitiveStats(UUID userID) {
        String token = preferences.getString("TOKEN", null);
        Call<FugitiveStat> call = services.getFugitiveStats(token, userID);

        call.enqueue(new Callback<FugitiveStat>() {
            @Override
            public void onResponse(Call<FugitiveStat> call, Response<FugitiveStat> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Fugitive stats successfully retrieved", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find your user account\nPlease re-login and try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "Unable to retrieve Fugitive stats\nPlease try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FugitiveStat> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getBountyHunterStats(UUID userID) {
        String token = preferences.getString("TOKEN", null);
        Call<BountyHunterStat> call = services.getBountyHunterStats(token, userID);

        call.enqueue(new Callback<BountyHunterStat>() {
            @Override
            public void onResponse(Call<BountyHunterStat> call, Response<BountyHunterStat> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "BountyHunter stats successfully retrieved", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find your user account\nPlease re-login and try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, "Unable to retrieve BountyHunter stats\nPlease try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BountyHunterStat> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n Please close the application and try again", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public interface FoundUserCallBack {
        void onUserReturned(User user);
    }

    public interface SearchUsersCallBack {
        void onUsersFound(List<User> user);
    }

    public interface registerCallBack {
        void registrationSuccess(Boolean success);
    }
}
