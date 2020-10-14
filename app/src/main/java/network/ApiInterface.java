package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.baroque.lujo.activities.country.CountryResponse;

import model.api_response.GenericResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ApiInterface {
    public static final String BaseUrl = "http://api-stage.golujo.com/v1/"; //staging
//  public static final String BaseUrl = "https://api.golujo.com/v1/"; //production

    private static GetServices getServices = null;

    public static GetServices getInstance() {
        if (getServices == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build())
                    .build();
            getServices = retrofit.create(GetServices.class);
        }
        return getServices;
    }

    public interface GetServices {

        //Returns the phone prefix details as provided by the BA API. Set `all` to get the entire list otherwise set the `id` you need data from.
        @GET("/phone-prefix/")
        Call<CountryResponse> getListOfCountries(@Query("id") String strID);

        //Used for the user signup.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users")
        Call<GenericResponse> signUp(@Field("firstname") String firstname,
                                     @Field("lastname") String lastname,
                                     @Field("email") String email,
                                     @Field("phone_prefix") String phone_prefix,
                                     @Field("phone") String phone);

        //User can verify his phone number using the code (SMS) he received during registration. Returns user `token`.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/verify")
        Call<GenericResponse> verifyOTP(@Field("phone_prefix") String phone_prefix,
                                        @Field("phone") String phone,
                                        @Field("code") String code
                                        );

        //ReSends OTP to the user to verify his phone number.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/verify-otp")
        Call<GenericResponse> resendOTP(@Field("phone_prefix") String phone_prefix,
                                        @Field("phone") String phone
        );

        //Sends SMS to the user to be used for login.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/login-otp")
        Call<GenericResponse> login(@Field("phone_prefix") String phone_prefix,
                                    @Field("phone") String phone
        );

        //User can verify his phone number using the code (SMS) he received during registration. Returns user `token`.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/login")
        Call<GenericResponse> verifyLoginOTP(@Field("phone_prefix") String phone_prefix,
                                             @Field("phone") String phone,
                                             @Field("code") String code
        );

        //Allow the user to change the phone he set during registration, but before activation.
        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/change-phone")
        Call<GenericResponse> changePhoneNumber(
                                            @Field("old_phone_prefix") String old_phone_prefix,
                                            @Field("old_phone") String old_phone,
                                            @Field("phone_prefix") String phone_prefix,
                                            @Field("phone") String phone
        );

        @GET("/users/profile")
        Call<GenericResponse> getUserProfile(@Query("token") String token);

        @FormUrlEncoded //(Required to use @Field parameter)
        @POST("/users/update")
        Call<GenericResponse> editProfile(
                @Field("firstname") String firstname,
                @Field("lastname") String lastname,
                @Field("email") String email,
                @Field("phone_prefix") String phone_prefix,
                @Field("phone") String phone,
                 @Field("token") String token
        );

//        @GET("stateM/{country_id}")
//        Call<EmirateResponse> getStatesByCountry(@Path("country_id") int country_id);
//
//        @GET("consultantByStateM/{emirate_id}")
//        Call<ConsultantResponse> getListOfConsultants(@Path("emirate_id") int emirateID);
//
//        @GET("type")
//        Call<Type> getTypes();
//
//        @FormUrlEncoded
//        @POST("signupM/mobileVerification")
//        Call<MobileSignUpResponse> mobileVerification(@Field("mobile") String mobile,
//                                                      @Field("countryId") int countryId);
//

//
//        @FormUrlEncoded
//        @POST("loginM")
//        Call<UserResponse> login(@Field("email") String email,
//                                 @Field("password") String password
//        );
//
//        @FormUrlEncoded
//        @POST("forgotM")
//        Call<UserResponse> forgotPassword(@Field("forgotType") String forgotType,
//                                          @Field("value") String value
//        );
//
//        @FormUrlEncoded
//        @POST("memberMobile/1")
//        Call<UserResponse> stepOne(@Field("userId") int userId,
//                                   @Field("email") String email,
//                                   @Field("emirateId") String emirateId,
//                                   @Field("family") String family,
//                                   @Field("isFamilyShow") Boolean isFamilyShow,
//                                   @Field("birthDate") String birthDate,
//                                   @Field("birthPlace") String birthPlace,
//                                   @Field("countryId") int countryId,
//                                   @Field("stateId") int stateId,
//                                   @Field("address") String address,
//                                   @Field("residenceTypeId") int residenceTypeId,
//                                   @Field("ethnicityId") int ethnicityId,
//                                   @Field("motherNationalityId") int motherNationalityId,
//                                   @Field("userUpdateId") int userUpdateId
//        );
//
//        @FormUrlEncoded
//        @POST("memberMobile/2")
//        Call<UserResponse> stepTwo(@Field("userId") int userId,
//                                   @Field("isSmokingId") int isSmokingId,
//                                   @Field("skinColorId") int skinColorId,
//                                   @Field("hairColorId") int hairColorId,
//                                   @Field("hairTypeId") int hairTypeId,
//                                   @Field("eyeColorId") int eyeColorId,
//                                   @Field("heightId") int heightId,
//                                   @Field("bodyTypeId") int bodyTypeId,
//                                   @Field("memberWeightId") int memberWeightId,
//                                   @Field("sectTypeId") int sectTypeId,
//                                   @Field("memberLicenseId") int memberLicenseId,
//                                   @Field("sBrideArrangmentId") int sBrideArrangmentId,
//                                   @Field("isPolygamy") String isPolygamy,
//                                   @Field("sCondemnBrideId") int sCondemnBrideId,
//                                   @Field("userUpdateId") int userUpdateId
//        );
//
//        @FormUrlEncoded
//        @POST("memberMobile/3")
//        Call<UserResponse> stepThree(@FieldMap Map<String, String> your_variable_name);
//
//        @FormUrlEncoded
//        @POST("memberMobile/4")
//        Call<UserResponse> stepFour(@FieldMap Map<String, String> your_variable_name);
//
//        @FormUrlEncoded
//        @POST("memberMobile/5")
//        Call<UserResponse> stepFive(@Field("userId") int userId,
//                                    @Field("firstRelative") String firstRelative,
//                                    @Field("firstRelativeNumber") String firstRelativeNumber,
//                                    @Field("firstRelativeRelationId") int firstRelativeRelationId,
//                                    @Field("secondRelative") String secondRelative,
//                                    @Field("secondRelativeNumber") String secondRelativeNumber,
//                                    @Field("secondRelativeRelationId") int secondRelativeRelationId,
//                                    @Field("applicantDescription") String applicantDescription,
//                                    @Field("languagesId") String languagesId,
//                                    @Field("signature") String signature,
//                                    @Field("userUpdateId") int userUpdateId
//        );
//
//        @FormUrlEncoded
//        @POST("paymentM/{userId}")
//        Call<UserPaymentResponse> getPaymentURL(
//                @Path("userId") int userId,
//                @Field("emailAddress") String emailAddress,
//                @Field("paymentType") int paymentType
//        );
//
//        @FormUrlEncoded
//        @POST("paymentStatusM/")
//        Call<UserResponse> getPaymentConfirmation(@Field("refNo") String refNo,
//                                                  @Field("userId") int userId
//        );
//
//        @GET("getData/matchingMembersByMemberIdM/{member_id}")
//        Call<UserResponse> getMatchingsByMemberID(@Path("member_id") int memberID);
//
//
//        @FormUrlEncoded
//        @POST("getData/getMemberDetailByIdM//")
//        Call<UserResponse> getMemberDetailsByMemberID(@Field("memberId") int refNo,
//                                                      @Field("conId") int conId
//        );
//
//        @FormUrlEncoded
//        @POST("signupM/changePassword")
//        Call<UserResponse> changePassword(@Field("id") int id,
//                                          @Field("password") String password,
//                                          @Field("oldPassword") String oldPassword
//        );
//
//        @FormUrlEncoded
//        @POST("update/memberPriority")
//        Call<UserResponse> changeMatchingsOrder(@Field("userId") int userId,
//                                                @Field("memberId") int memberId,
//                                                @Field("priorityId") int priorityId,
//                                                @Field("memberIdShift") int memberIdShift,
//                                                @Field("priorityIdShift") int priorityIdShift,
//                                                @Field("userUpdateId") int userUpdateId,
//                                                @Field("deviceInfo") String deviceInfo,
//                                                @Field("appVersion") String appVersion
//        );
//
//        @FormUrlEncoded
//        @POST("delete/memberPriority")
//        Call<UserResponse> deleteMatching(@Field("userId") int userId,
//                                          @Field("memberId") int memberId,
//                                          @Field("userUpdateId") int userUpdateId,
//                                          @Field("deviceInfo") String deviceInfo,
//                                          @Field("appVersion") String appVersion
//        );

    }
}
