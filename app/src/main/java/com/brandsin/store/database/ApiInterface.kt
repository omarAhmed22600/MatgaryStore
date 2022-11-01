package com.brandsin.store.database

import com.brandsin.store.model.IntroResponse
import com.brandsin.store.model.auth.StoreTagsRequest
import com.brandsin.store.model.auth.StoreTagsResponse
import com.brandsin.store.model.auth.StoreTypeResponse
import com.brandsin.store.model.auth.condition.ConditionsResponse
import com.brandsin.store.model.auth.devicetoken.DeviceTokenRequest
import com.brandsin.store.model.auth.devicetoken.DeviceTokenResponse
import com.brandsin.store.model.auth.forgot.ForgotPassRequest
import com.brandsin.store.model.auth.forgot.ForgotPassResponse
import com.brandsin.store.model.auth.login.LoginRequest
import com.brandsin.store.model.auth.login.LoginResponse
import com.brandsin.store.model.auth.register.RegisterResponse
import com.brandsin.store.model.auth.resendcode.ResendCodeRequest
import com.brandsin.store.model.auth.resendcode.ResendCodeResponse
import com.brandsin.store.model.auth.resetpass.ResetPassRequest
import com.brandsin.store.model.auth.resetpass.ResetPassResponse
import com.brandsin.store.model.auth.setting.countryId.CountryIdResponse
import com.brandsin.store.model.auth.verifycode.VerifyCodeRequest
import com.brandsin.store.model.auth.verifycode.VerifyCodeResponse
import com.brandsin.store.model.main.homepage.OldOrdersResponse
import com.brandsin.store.model.main.homepage.OrderDetailsResponse
import com.brandsin.store.model.menu.commonquest.CommonQuesResponse
import com.brandsin.store.model.menu.notifications.ReadNotificationRequest
import com.brandsin.store.model.main.offers.add.CreateOfferResponse
import com.brandsin.store.model.main.offers.add.OfferAddProductRequest
import com.brandsin.store.model.main.offers.add.OfferAddProductResponse
import com.brandsin.store.model.main.offers.delete.DeleteOfferRequest
import com.brandsin.store.model.main.offers.delete.DeleteOfferResponse
import com.brandsin.store.model.main.offers.listoffer.OffersResponse
import com.brandsin.store.model.main.offers.update.UpdateOfferRequest
import com.brandsin.store.model.main.offers.update.UpdateOfferResponse
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderRequest
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderResponse
import com.brandsin.store.model.main.products.ListUnitResponse
import com.brandsin.store.model.main.products.list.ListProductsResponse
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.model.main.products.add.AddProductResponse
import com.brandsin.store.model.main.products.delete.DeleteProductRequest
import com.brandsin.store.model.main.products.delete.DeleteProductResponse
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesResponse
import com.brandsin.store.model.main.reports.ReportsDetailsResponse
import com.brandsin.store.model.main.reports.ReportsTotalResponse
import com.brandsin.store.model.menu.MenuBusyRequest
import com.brandsin.store.model.menu.MenuClosedRequest
import com.brandsin.store.model.menu.MenuResponse
import com.brandsin.store.model.menu.connectingmain.ConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.ConnectingMainResponse
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainResponse
import com.brandsin.store.model.menu.connectingmain.list.ListConnectingMainResponse
import com.brandsin.store.model.profile.addedstories.deletestory.DeleteStoryRequest
import com.brandsin.store.model.profile.addedstories.deletestory.DeleteStoryResponse
import com.brandsin.store.model.profile.addedstories.liststories.ListStoriesResponse
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryWithoutRequest
import com.brandsin.store.model.profile.changePass.ChangePassRequest
import com.brandsin.store.model.profile.changePass.ChangePassResponse
import com.brandsin.store.model.profile.updateprofile.UpdateProfileRequest
import com.brandsin.store.model.profile.updateprofile.UpdateProfileResponse
import com.brandsin.store.model.profile.updatestore.UpdateStoreResponse
import com.brandsin.store.model.profile.updatestore.UploadResponse
import com.brandsin.user.model.menu.help.HelpQuesResponse
import com.brandsin.user.model.menu.notifications.NotificationResponse
import com.brandsin.user.model.menu.notifications.ReadNotificationResponse
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import com.brandsin.user.model.menu.settings.SocialLinksResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface
{
    @Multipart
    @POST("/api/hajaty/store/register")
    suspend fun register(
        @Part("store[name]") storeName: RequestBody,
        @Part("store[lat]") storeLat: RequestBody,
        @Part("store[lng]") storeLng: RequestBody,
        @Part("store[address]") address: RequestBody,
        @Part("store[delivery_price]") deliveryPrice: RequestBody,
        @Part("store[delivery_time]") deliveryTime: RequestBody,
        @Part("store[delivery_distance]") delivery_distance: RequestBody,
        @Part("store[min_order_price]") min_order_price: RequestBody,
        @Part("store[phone_number]") phone_number: RequestBody,
        @Part("store[whatsapp]") whatsapp: RequestBody,
        @Part("user[name]") userName: RequestBody,
        @Part("user[last_name]") userLastName: RequestBody,
        @Part("user[phone_number]") userPhoneNumber: RequestBody,
        @Part("user[email]") userEmail: RequestBody,
        @Part("user[password]") userPassword: RequestBody,
        @Part("categories[]") category: ArrayList<Int>,
        @Part("tags[]") tags: ArrayList<Int>,
        @Part("store[has_delivery]") hasDelivery: RequestBody,
        @Part image: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part ): RegisterResponse

    @Multipart
    @POST("/api/hajaty/store/register")
    suspend fun register2(
        @Part("store[name]") storeName: RequestBody,
        @Part("store[lat]") storeLat: RequestBody,
        @Part("store[lng]") storeLng: RequestBody,
        @Part("store[address]") address: RequestBody,
        @Part("store[delivery_price]") deliveryPrice: RequestBody,
        @Part("store[delivery_time]") deliveryTime: RequestBody,
        @Part("store[delivery_distance]") delivery_distance: RequestBody,
        @Part("store[min_order_price]") min_order_price: RequestBody,
        @Part("store[phone_number]") phone_number: RequestBody,
        @Part("store[whatsapp]") whatsapp: RequestBody,
        @Part("user[name]") userName: RequestBody,
        @Part("user[last_name]") userLastName: RequestBody,
        @Part("user[phone_number]") userPhoneNumber: RequestBody,
        @Part("user[email]") userEmail: RequestBody,
        @Part("user[password]") userPassword: RequestBody,
        @Part("categories[]") category: ArrayList<Int>,
        @Part("tags[]") tags: ArrayList<Int>,
        @Part("store[has_delivery]") hasDelivery: RequestBody,
        @Part imageCommercial: MultipartBody.Part,
        @Part imageNationalId: MultipartBody.Part): RegisterResponse

    @GET("/api/common/settings")
    suspend fun getConditions(@Query("code") code: String, @Query("lang") lang: String): ConditionsResponse

    /* ---------- Auth APIs -------- */
    // login
    @POST("/api/hajaty/store/login")
    fun login(@Body loginRequest: LoginRequest?): Call<LoginResponse?>

    // country_id+Condition
    @GET("/api/common/settings")
    fun getCountryId(
            @Query("code") code: String?,
            @Query("lang") lang: String?
    ): Call<CountryIdResponse?>?
    // verify
    @POST("/api/users/check_code")
    fun verify(@Body verifyCodeRequest: VerifyCodeRequest?): Call<VerifyCodeResponse?>
    // resendCode
    @POST("/api/users/resend_code")
    fun resendCode(@Body resendCodeRequest: ResendCodeRequest?): Call<ResendCodeResponse?>
    // forgotPass
    @POST("/api/users/forget_password")
    fun forgotPass(@Body forgotPassRequest: ForgotPassRequest?): Call<ForgotPassResponse?>
    // resetPass
    @POST("/api/users/update_password")
    fun resetPass(@Body resetPassRequest: ResetPassRequest?): Call<ResetPassResponse?>
    // changePass
    @POST("/api/users/update_user")
    fun changePass(@Body changePassRequest: ChangePassRequest?): Call<ChangePassResponse?>
    // updateProfile
    @POST("/api/users/update_user")
    fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest?): Call<UpdateProfileResponse?>
    // deviceToken
    @POST("/api/users/update_token")
    fun deviceToken(@Body deviceTokenRequest: DeviceTokenRequest?): Call<DeviceTokenResponse?>



    @GET("/api/hajaty/offers")
    suspend fun getOffers(@Query("store_id") storeId: Int, @Query("limit") limit: Int, @Query("page") page: Int,
                            @Query("locale") lang: String): OffersResponse

    @Multipart
    @POST("/api/hajaty/offers")
     suspend fun createOffer(@Part("store_id") store_id: RequestBody,
                             @Part("name") name: RequestBody,
                             @Part("description") description: RequestBody,
                             @Part("name_en") nameEn: RequestBody,
                             @Part("description_en") descriptionEn: RequestBody,
                             @Part("price") price: RequestBody,
                             @Part("price_to") price_to: RequestBody,
                             @Part("start_date") start_date: RequestBody,
                             @Part("end_date") end_date: RequestBody,
                             @Part("active") active: RequestBody,
                             @Part("products[]") products: ArrayList<Int>,
                             @Part image: MultipartBody.Part,
                             @Part("locale") locale: RequestBody): CreateOfferResponse

    @POST("/api/hajaty/offers/delete")
    suspend fun deleteOffer(@Body deleteOfferRequest: DeleteOfferRequest): DeleteOfferResponse

    @POST("/api/hajaty/offers")
    suspend fun updateOfferWithoutImage(@Body updateRequest: UpdateOfferRequest): UpdateOfferResponse

    @Multipart
    @POST("/api/hajaty/offers")
    suspend fun updateOffer(@Part("id") id: RequestBody,
                            @Part("store_id") store_id: RequestBody,
                            @Part("name") name: RequestBody,
                            @Part("description") description: RequestBody,
                            @Part("name_en") nameEn: RequestBody,
                            @Part("description_en") descriptionEn: RequestBody,
                            @Part("price") price: RequestBody,
                            @Part("price_to") price_to: RequestBody,
                            @Part("start_date") start_date: RequestBody,
                            @Part("end_date") end_date: RequestBody,
                            @Part("active") active: RequestBody,
                            @Part("products[]") products: ArrayList<Int>,
                            @Part image: MultipartBody.Part,
                            @Part("locale") locale: RequestBody): UpdateOfferResponse

    @GET("/api/hajaty/store/show")
    suspend fun getStoreProducts(@Query("store_id") storeId: Int, @Query("locale") locale: String ,
                                 @Query("page") page: Int , @Query("limit") limit: Int): ListProductsResponse

    @Multipart
    @POST("/api/hajaty/product")
    suspend fun createProduct(@Part("name") name: RequestBody,
                              @Part("description") description: RequestBody,
                              @Part("name_en") nameEn: RequestBody,
                              @Part("description_en") descriptionEn: RequestBody,
                              @Part("type") type: RequestBody,
                              @Part("status") status: RequestBody,
                              @Part("product_status") productStatus: RequestBody,
                              @Part("store_id") storeId: RequestBody,
                              @Part("skus") skus:RequestBody,
                              @Part("categories[]") categories: ArrayList<Int>,
                              @Part("device") device: RequestBody,
                              @Part("media_id[]") storeMedia: ArrayList<Int>,
                              @Part("delete_media_id[]") deleteMedia: ArrayList<Int>,
                              @Part("locale") locale: RequestBody): AddProductResponse


    @POST("/api/hajaty/product/delete")
    suspend fun deleteProduct(@Body deleteProductRequest: DeleteProductRequest): DeleteProductResponse

    @Multipart
    @POST("/api/hajaty/product")
    suspend fun updateProduct(@Part("id") id: RequestBody,
                              @Part("name") name: RequestBody,
                              @Part("description") description: RequestBody,
                              @Part("name_en") nameEn: RequestBody,
                              @Part("description_en") descriptionEn: RequestBody,
                              @Part("type") type: RequestBody,
                              @Part("status") status: RequestBody,
                              @Part("product_status") productStatus: RequestBody,
                              @Part("store_id") storeId: RequestBody,
                              @Part("skus") skus:RequestBody,
                              @Part("categories[]") categories: ArrayList<Int>,
                              @Part("device") device: RequestBody,
                              @Part("media_id[]") storeMedia: ArrayList<Int>,
                              @Part("delete_media_id[]") deleteMedia: ArrayList<Int>,
                              @Part("locale") locale: RequestBody): UpdateProductResponse


    @GET("/api/categories/all_categories")
    suspend fun getProductCategories(@Query("parents_only") parents_only : Int,
                                     @Query("lang") lang : String,
                                     @Query("store_id") storeId: Int): ProductCategoriesResponse

    @GET("/api/hajaty/notifications")
    suspend fun getNotifications(@Query("limit") limit: Int, @Query("page") page: Int,
                          @Query("user_id") user_id: Int): NotificationResponse

    @POST("/api/hajaty/notifications")
    suspend fun readNotification(@Body request : ReadNotificationRequest): ReadNotificationResponse

    @GET("/api/hajaty/pages")
    suspend fun getCommonQues(@Query("type") type: String, @Query("lang") lang: String): CommonQuesResponse

    @GET("/api/hajaty/pages")
    suspend fun getHelpQues(@Query("type") type: String, @Query("lang") lang: String): HelpQuesResponse

    @GET("/api/orders/list_orders")
    suspend fun getStoreOrders(@Query("lang") lang: String, @Query("store_id") storeId: Int, @Query("limit") limit : Int,
                             @Query("status") status: String, @Query("page") page : Int): OldOrdersResponse


    @GET("/api/common/settings")
    suspend fun getPhoneNumber(@Query("code") code: String, @Query("lang") lang: String): PhoneNumberResponse

    @GET("/api/common/settings")
    suspend fun getSocialLinks(@Query("code") code: String, @Query("lang") lang: String): SocialLinksResponse


    @GET("/api/orders/order_details")
    suspend fun getOrderDetails(@Query("order_id") id : Int, @Query("lang") lang : String): OrderDetailsResponse



    @GET("/api/hajaty/store/list_shop_categories")
    suspend fun getStoreTypes(@Query("locale") lang: String): StoreTypeResponse

    @Multipart
    @POST("/api/hajaty/store/update")
    suspend fun updateStoreInfo(
            @Part("store[id]") storeId: RequestBody,
            @Part("store[name]") storeName: RequestBody,
            @Part("store[lat]") storeLat: RequestBody,
            @Part("store[lng]") storeLng: RequestBody,
            @Part("store[address]") address: RequestBody,
            @Part("store[delivery_price]") deliveryPrice: RequestBody,
            @Part("store[delivery_time]") deliveryTime: RequestBody,
            @Part("store[delivery_distance]") delivery_distance: RequestBody,
            @Part("store[min_order_price]") min_order_price: RequestBody,
            @Part("store[phone_number]") phone_number: RequestBody,
            @Part("store[whatsapp]") whatsapp: RequestBody,
            @Part("categories[]") category: ArrayList<Int>,
            @Part("tags[]") tags: ArrayList<Int>,
            @Part("store[has_delivery]") hasDelivery: RequestBody,
            @Part("store_media[]") storeMedia: ArrayList<Int>,
            @Part("delete_media[]") deleteMedia: ArrayList<Int>
    ): UpdateStoreResponse

    // OfferAddProduct
    @POST("/api/products/search_products")
    suspend fun setOfferAddProduct(@Body offerAddProductRequest: OfferAddProductRequest): OfferAddProductResponse

    // Update Status
    @POST("/api/hajaty/order/update_status")
    suspend fun setUpdateStatusOrder(@Body updateStatusOrderRequest: UpdateStatusOrderRequest): UpdateStatusOrderResponse

    //reportsTotal
    @GET("/api/hajaty/reports/total")
    suspend fun getReportsTotal(
            @Query("store_id") store_id: Int,
            @Query("limit") limit: Int,
            @Query("page") page: Int,
            @Query("type") type: String,
            @Query("from") from: String,
            @Query("to") to: String
    ): ReportsTotalResponse

    //reportsDetails
    @GET("/api/hajaty/reports/detailed")
    suspend fun getReportsDetails(
            @Query("store_id") store_id: Int,
            @Query("limit") limit: Int,
            @Query("page") page: Int,
            @Query("type") type: String,
            @Query("from") from: String,
            @Query("to") to: String
    ): ReportsDetailsResponse


    @POST("/api/hajaty/store/tags")
    fun getTags(@Body request : StoreTagsRequest): Call<StoreTagsResponse?>

    @POST("/api/hajaty/store/is_busy")
    fun setMenuBusy(@Body request : MenuBusyRequest): Call<MenuResponse?>

    @POST("/api/hajaty/store/is_closed")
    fun setMenuClosed(@Body request : MenuClosedRequest): Call<MenuResponse?>

   @GET("/api/hajaty/list_units")
    suspend fun getUnitsList(
            @Query("categories[]") category: ArrayList<Int>,
            @Query("locale") locale: String
    ): ListUnitResponse

    @POST("/api/hajaty/clone_products")
    fun setConnectingMain(@Body request : ConnectingMainRequest): Call<ConnectingMainResponse?>

    @GET("/api/hajaty/list_clone_products_requests")
    fun getListConnectingMain(
        @Query("store_id") store_id: Int
    ): Call<ListConnectingMainResponse?>

    @POST("/api/hajaty/accept_clone_products_request")
    fun setAcceptConnectingMain(@Body request : AcceptConnectingMainRequest): Call<AcceptConnectingMainResponse?>

    @Multipart
    @POST("/api/upload")
    suspend fun upload(
            @Part("collection") collection: RequestBody,
            @Part image: MultipartBody.Part ): UploadResponse

    // ListStories
    @GET("/api/hajaty/store/list_stories")
    fun getListStories(
        @Query("store_id") store_id: Int
    ): Call<ListStoriesResponse?>

    // DeleteStories
    @POST("/api/hajaty/store/delete_story")
    fun deleteStories(@Body deleteStoryRequest: DeleteStoryRequest?): Call<DeleteStoryResponse?>

    // UploadStories
    @Multipart
    @POST("/api/hajaty/store/upload_story")
    suspend fun uploadStories(
        @Part("store_id") storeId: RequestBody,
        @Part("text") text: RequestBody,
        @Part image: MultipartBody.Part ): UploadStoryResponse

    @POST("/api/hajaty/store/upload_story")
    suspend fun uploadStoriesWithout(
        @Body uploadStoryWithoutRequest: UploadStoryWithoutRequest
    ): UploadStoryResponse

    //Intro
    @GET("/api/common/introduction_app")
    fun getIntro(@Query("app") app: String ): Call<IntroResponse?>

}