package ru.app.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.app.models.addfield.AddFieldHoursResponse
import ru.app.models.addfield.AddFieldResponse
import ru.app.models.addfield.AddHoursRequest
import ru.app.models.auth.*
import ru.app.models.blacklist.BlackListCreate
import ru.app.models.blacklist.BlackListResponse
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingCreate
import ru.app.models.booking.FieldBookingResponse
import ru.app.models.booking.FieldReviewRequest
import ru.app.models.booking.requests.RequestStatus
import ru.app.models.fields.FieldListItem
import ru.app.models.fields.FieldReviewsItem
import ru.app.models.fields.userfields.UserFieldsResponseItem
import ru.app.models.fieldtypes.FieldTypesResponse
import ru.app.models.news.NewsListItem

interface ApiService {

    /**    GET   */
    @GET("api/main/news/")
    fun getNewsList(): Call<MutableList<NewsListItem>>

    @GET("api/main/field/")
    fun getFieldsList(): Call<MutableList<FieldListItem>>

//    @GET("api/main/field/{id}")
//    fun getFieldListItemById(@Path("id") id: Int): Call<FieldListItem>

    @GET("api/main/field/{id}")
    fun getFieldListItemById(@Header("Authorization")token:String,
                             @Path("id") id: Int): Call<FieldListItem>

    @GET("api/main/field/")
    fun getFieldsList(@Query("ordering") ordering: String): Call<MutableList<FieldListItem>>

    @GET("api/main/field_review/")
    fun getFieldReviewsList(@Query("field") fieldId: String): Call<MutableList<FieldReviewsItem>>

    //@Headers("Content-Type:application/json")
    @GET("api/main/user/fields/")
    fun getUserFields(@Header("Authorization") token: String) : Call<MutableList<UserFieldsResponseItem>>

    @GET("api/main/blacklist/")
    fun getBanedUsers(@Header("Authorization")token: String) : Call<List<BlackListResponse>>

    //getBookings
    @GET("api/main/field_booking/")
    fun getBookings(@Header("Authorization")token: String) : Call<ArrayList<BookingItem>>

    @GET("api/main/owner/requests/")
    fun getRequests(@Header("Authorization")token: String,
                    @Query("status")status: Int): Call<ArrayList<BookingItem>>

    @Headers("Content-Type:application/json")
    @GET("api/main/field_type/")
    fun getFieldTypes(): Call<FieldTypesResponse>

    /**    POST   */
    @Multipart
    @POST("api/main/field/create/")
    fun createField(@Header("Authorization") token: String,
                    @Part("field_type") fieldType: RequestBody,
                    @Part("name") name: RequestBody,
                    @Part("price") price: RequestBody,
                    @Part("minimum_size") minimumSize: RequestBody,
                    @Part("maximum_size") maximumSIze: RequestBody,
                    @Part   images: List<MultipartBody.Part>,
                    @Part("location") location: RequestBody,
                    @Part("description") description: RequestBody,
                    @Part("number_of_players") numberOfPlayers: RequestBody,
                    @Part("has_parking") hasParking: RequestBody,
                    @Part("is_indoor") isIndoor: RequestBody,
                    @Part("has_showers") hasShowers: RequestBody,
                    @Part("has_locker_rooms") hasLockerRooms: RequestBody,
                    @Part("has_lights") hasLights: RequestBody,
                    @Part("has_rostrum") hasRostrum: RequestBody,
                    @Part("has_equipment") hasEquipment: RequestBody,
                    @Part("disable_booking") disable_booking: RequestBody,
                    @Part("phone_number") phoneNumber: RequestBody
    ): Call<AddFieldResponse>

    @POST("api/main/field/hours/")
    fun createFieldHours(@Header("Authorization") token: String, @Body request: AddHoursRequest): Call<AddFieldHoursResponse>

    //login
    @Headers("Content-Type:application/json")
    @POST("api/auth/token/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    //register
    @Headers("Content-Type:application/json")
    @POST("api/auth/register/")
    fun register(@Body request: UserCreate): Call<UserCreateResponse>

    @Headers("Content-Type:application/json")
    @POST("api/auth/refresh/")
    fun refreshToken(@Body token: RefreshToken) : Call<RefreshToken>

    //Booking field
    @Headers("Content-Type:application/json")
    @POST("api/main/field_booking/")
    fun bookTheField(@Header("Authorization")token: String,
                     @Body bookingFieldCreate : FieldBookingCreate) : Call<FieldBookingResponse>


    @Headers("Content-Type:application/json")
    @POST("api/main/blacklist/")
    fun addToBlackList(@Header("Authorization")token: String,
                       @Body ban : BlackListCreate) : Call<BlackListResponse>

    @POST("api/main/field_review/")
    fun createFieldReview(@Header("Authorization") token: String, @Body request: FieldReviewRequest): Call<FieldReviewsItem>

    /**    PUT   */
    @PUT("api/main/field/{id}/hours/")
    fun updateFieldHours(@Header("Authorization") token: String, @Body request: AddHoursRequest,@Path("id") productId: Int): Call<AddFieldHoursResponse>

    /**    DELETE   */
    @DELETE("api/main/field/{id}/")
    fun deleteField(@Path("id") id: Int) : Call<Void>

    @DELETE("api/main/owner/requests/{id}/")
    fun deleteAllRequestsFromUser(@Header("Authorization") token: String,
                                  @Path ("id") id: Int) : Call<Void>

    @DELETE("api/main/blacklist/{id}/")
    fun unblockUser(@Header("Authorization")token: String,
                    @Path("id")id: Int): Call<ResponseBody>

    @DELETE("api/main/field_booking/{id}/")
    fun deleteRequest(@Header("Authorization")token: String,
                      @Path("id")id: Int): Call<ResponseBody>

    /**    PATCH   */
    @Headers("Content-Type:application/json")
    @PATCH("api/auth/users/")
    fun updateClientData(@Header("Authorization")token: String,
                         @Body user:UserCreate) : Call<UserCreateResponse>

    @Headers("Content-Type:application/json")
    @PATCH("api/main/field_booking/{id}/")
    fun request(@Header("Authorization")token: String,
                @Path("id")id: Int,
                @Body request: RequestStatus
    ) : Call<FieldBookingResponse>

    @Multipart
    @PATCH("api/main/field/{id}/")
    fun updateField(@Header("Authorization") token: String,
                    @Part("field_type") fieldType: RequestBody,
                    @Part("name") name: RequestBody,
                    @Part("price") price: RequestBody,
                    @Part("minimum_size") minimumSize: RequestBody,
                    @Part("maximum_size") maximumSIze: RequestBody,
                    @Part   images: List<MultipartBody.Part>,
                    @Part("location") location: RequestBody,
                    @Part("description") description: RequestBody,
                    @Part("number_of_players") numberOfPlayers: RequestBody,
                    @Part("has_parking") hasParking: RequestBody,
                    @Part("is_indoor") isIndoor: RequestBody,
                    @Part("has_showers") hasShowers: RequestBody,
                    @Part("has_locker_rooms") hasLockerRooms: RequestBody,
                    @Part("has_lights") hasLights: RequestBody,
                    @Part("has_rostrum") hasRostrum: RequestBody,
                    @Part("has_equipment") hasEquipment: RequestBody,
                    @Part("disable_booking") disable_booking: RequestBody,
                    //@Part("is_hidden") isHidden: RequestBody,
                    @Path("id") productId: Int,
                    @Part("phone_number") phoneNumber: RequestBody
    ): Call<AddFieldResponse>
    @Multipart
    @PATCH("api/main/field/{id}/")
    fun updateField(@Header("Authorization") token: String,
                    @Part("field_type") fieldType: RequestBody,
                    @Part("name") name: RequestBody,
                    @Part("price") price: RequestBody,
                    @Part("minimum_size") minimumSize: RequestBody,
                    @Part("maximum_size") maximumSIze: RequestBody,
                    @Part("location") location: RequestBody,
                    @Part("description") description: RequestBody,
                    @Part("number_of_players") numberOfPlayers: RequestBody,
                    @Part("has_parking") hasParking: RequestBody,
                    @Part("is_indoor") isIndoor: RequestBody,
                    @Part("has_showers") hasShowers: RequestBody,
                    @Part("has_locker_rooms") hasLockerRooms: RequestBody,
                    @Part("has_lights") hasLights: RequestBody,
                    @Part("has_rostrum") hasRostrum: RequestBody,
                    @Part("has_equipment") hasEquipment: RequestBody,
                    @Part("disable_booking") disable_booking: RequestBody,
                    //@Part("is_hidden") isHidden: RequestBody,
                    @Path("id") productId: Int,
                    @Part("phone_number") phoneNumber: RequestBody
    ): Call<AddFieldResponse>
}

