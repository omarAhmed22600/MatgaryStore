package com.brandsin.store.database

import com.brandsin.store.model.BaseResponse
import com.brandsin.store.model.IntroResponse
import com.brandsin.store.model.MessageResponse
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
import com.brandsin.store.model.main.products.add.AddProductResponse
import com.brandsin.store.model.main.products.delete.DeleteProductRequest
import com.brandsin.store.model.main.products.delete.DeleteProductResponse
import com.brandsin.store.model.main.products.list.ListProductsResponse
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesResponse
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.model.main.reports.ReportsDetailsResponse
import com.brandsin.store.model.main.reports.ReportsTotalResponse
import com.brandsin.store.model.menu.MenuBusyRequest
import com.brandsin.store.model.menu.MenuClosedRequest
import com.brandsin.store.model.menu.MenuResponse
import com.brandsin.store.model.menu.commonquest.CommonQuesResponse
import com.brandsin.store.model.menu.connectingmain.ConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.ConnectingMainResponse
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainResponse
import com.brandsin.store.model.menu.connectingmain.list.ListConnectingMainResponse
import com.brandsin.store.model.menu.notifications.ReadNotificationRequest
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
import com.brandsin.store.ui.main.addproduct.ProductAttributesResponse
import com.brandsin.store.ui.main.categories.model.CategoriesListResponse
import com.brandsin.store.ui.main.discountCoupon.model.CouponListResponse
import com.brandsin.store.ui.main.discountCoupon.model.CreateAndUpdateCouponResponse
import com.brandsin.store.ui.main.marketingRequest.model.PinStoriesMarketingResponse
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureDetailsResponse
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureListResponse
import com.brandsin.store.ui.main.refundableProduct.model.RefundableDetailsResponse
import com.brandsin.store.ui.main.refundableProduct.model.RefundableProductResponse
import com.brandsin.store.ui.main.subscriptions.SubscriptionListResponse
import com.brandsin.store.ui.menu.wallet.model.WalletTransactionsResponse
import com.brandsin.user.model.menu.help.HelpQuesResponse
import com.brandsin.user.model.menu.notifications.NotificationResponse
import com.brandsin.user.model.menu.notifications.ReadNotificationResponse
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import com.brandsin.user.model.menu.settings.SocialLinksResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @Multipart
    @POST("/api/hajaty/store/register")
    suspend fun register(
        @Part("store[name]") storeName: RequestBody,
        @Part("store[lat]") storeLat: RequestBody,
        @Part("store[lng]") storeLng: RequestBody,
        @Part("store[address]") address: RequestBody,
        @Part("store[delivery_price]") deliveryPrice: RequestBody?,
        @Part("store[delivery_time]") deliveryTime: RequestBody?,
        @Part("store[delivery_type]") deliveryType: RequestBody?,
        @Part("store[delivery_distance]") deliveryDistance: RequestBody?,
        @Part("store[min_order_price]") minOrderPrice: RequestBody,
        @Part("store[phone_number]") phoneNumber: RequestBody,
        @Part("store[whatsapp]") whatsapp: RequestBody,
        @Part("user[name]") userName: RequestBody,
        @Part("user[last_name]") userLastName: RequestBody,
        @Part("user[phone_number]") userPhoneNumber: RequestBody,
        @Part("user[email]") userEmail: RequestBody,
        @Part("user[password]") userPassword: RequestBody,
        @Part("categories[]") category: ArrayList<Int>,
        @Part("tags[]") tags: ArrayList<Int>,
        @Part("store[has_delivery]") hasDelivery: RequestBody,
        @Part("store[pick_up_from_store]") pickUpFromStore: RequestBody,
        @Part("store[cash_on_delivery]") cashOnDelivery: RequestBody,
        // banking data
        @Part("store[owner_name]") storeOwnerName: RequestBody,
        @Part("store[bank_name]") storeBankName: RequestBody,
        @Part("store[iban]") storeIban: RequestBody,
        // Images
        @Part image: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part
    ): RegisterResponse

    @Multipart
    @POST("/api/hajaty/store/register")
    suspend fun register2(
        @Part("store[name]") storeName: RequestBody,
        @Part("store[lat]") storeLat: RequestBody,
        @Part("store[lng]") storeLng: RequestBody,
        @Part("store[address]") address: RequestBody,
        @Part("store[delivery_price]") deliveryPrice: RequestBody,
        @Part("store[delivery_time]") deliveryTime: RequestBody,
        @Part("store[delivery_distance]") deliveryDistance: RequestBody,
        @Part("store[min_order_price]") minOrderPrice: RequestBody,
        @Part("store[phone_number]") phoneNumber: RequestBody,
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
        @Part imageNationalId: MultipartBody.Part
    ): RegisterResponse

    @GET("/api/common/settings")
    suspend fun getConditions(
        @Query("code") code: String,
        @Query("lang") lang: String
    ): ConditionsResponse

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
    @FormUrlEncoded
    @POST("api/common/send_notification")
    suspend fun sendNotification(
        @Field("message") message:String,
        @Field("user_id") userId:Int,
        @Field("click_action_key") clickActionKey:String,
        @Field("click_action_id") clickActionId:Int,
    ): ReadNotificationResponse?
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
    fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest?
    ): Call<UpdateProfileResponse?>

    // deviceToken
    @POST("/api/users/update_token")
    fun deviceToken(@Body deviceTokenRequest: DeviceTokenRequest?): Call<DeviceTokenResponse?>

    @GET("/api/hajaty/offers")
    suspend fun getOffers(
        @Query("store_id") storeId: Int, @Query("limit") limit: Int, @Query("page") page: Int,
        @Query("locale") lang: String
    ): OffersResponse

    @Multipart
    @POST("/api/hajaty/offers")
    suspend fun createOffer(
        @Part("store_id") storeId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("name_en") nameEn: RequestBody,
        @Part("description_en") descriptionEn: RequestBody,
        @Part("price") price: RequestBody,
        @Part("price_to") priceTo: RequestBody,
        @Part("start_date") startDate: RequestBody,
        @Part("end_date") endDate: RequestBody,
        @Part("active") active: RequestBody,
        @Part("type") type: RequestBody,
        @Part("products[]") products: ArrayList<Int>? = null,
        @Part image: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
        @Part("locale") locale: RequestBody
    ): CreateOfferResponse

    @POST("/api/hajaty/offers/delete")
    suspend fun deleteOffer(@Body deleteOfferRequest: DeleteOfferRequest): DeleteOfferResponse

    @POST("/api/hajaty/offers")
    suspend fun updateOfferWithoutImage(@Body updateRequest: UpdateOfferRequest): UpdateOfferResponse

    @Multipart
    @POST("/api/hajaty/offers")
    suspend fun updateOffer(
        @Part("id") id: RequestBody,
        @Part("store_id") storeId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("name_en") nameEn: RequestBody,
        @Part("description_en") descriptionEn: RequestBody,
        @Part("price") price: RequestBody,
        @Part("price_to") priceTo: RequestBody,
        @Part("start_date") startDate: RequestBody,
        @Part("end_date") endDate: RequestBody,
        @Part("active") active: RequestBody,
        @Part("type") type: RequestBody,
        @Part("products[]") products: ArrayList<Int>? = null,
        @Part image: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
        @Part("locale") locale: RequestBody
    ): UpdateOfferResponse

    @GET("/api/hajaty/store/show")
    suspend fun getStoreProducts(
        @Query("store_id") storeId: Int,
        @Query("locale") locale: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ListProductsResponse

    @Multipart
    @POST("/api/hajaty/product")
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("name_en") nameEn: RequestBody,
        @Part("description_en") descriptionEn: RequestBody,
        @Part("type") type: RequestBody,
        @Part("status") status: RequestBody,
        @Part("product_status") productStatus: RequestBody,
        @Part("store_id") storeId: RequestBody,
        @Part("skus") skus: RequestBody,
        @Part("categories[]") categories: ArrayList<Int>,
        @Part("device") device: RequestBody,
        @Part("media_id[]") storeMedia: ArrayList<Int>,
        @Part("delete_media_id[]") deleteMedia: ArrayList<Int>,
        @Part("locale") locale: RequestBody
    ): AddProductResponse
    @POST("api/product/update_status/{id}")
    suspend fun changeProductStatus(
        @Path("id") id: Int,
    ): CommonQuesResponse

    @POST("/api/hajaty/product/delete")
    suspend fun deleteProduct(@Body deleteProductRequest: DeleteProductRequest): DeleteProductResponse

    @Multipart
    @POST("/api/hajaty/product")
    suspend fun updateProduct(
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("name_en") nameEn: RequestBody,
        @Part("description_en") descriptionEn: RequestBody,
        @Part("type") type: RequestBody,
        @Part("status") status: RequestBody,
        @Part("product_status") productStatus: RequestBody,
        @Part("store_id") storeId: RequestBody,
        @Part("skus") skus: RequestBody,
        @Part("categories[]") categories: ArrayList<Int>,
        @Part("device") device: RequestBody,
        @Part("media_id[]") storeMedia: ArrayList<Int>,
        @Part("delete_media_id[]") deleteMedia: ArrayList<Int>,
        @Part("locale") locale: RequestBody
    ): UpdateProductResponse


    @GET("/api/categories/all_categories")
    suspend fun getProductCategories(
        @Query("parents_only") parentsOnly: Int,
        @Query("lang") lang: String,
        @Query("store_id") storeId: Int
    ): ProductCategoriesResponse
    @GET("/api/hajaty/attr")
    suspend fun getProductAttrs(
        @Query("store_id") storeId: Int
    ): Response<ProductAttributesResponse>
    @GET("/api/hajaty/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int, @Query("page") page: Int,
        @Query("user_id") userId: Int,@Query("store_id") storeId: Int
    ): NotificationResponse

    @POST("/api/hajaty/notifications")
    suspend fun readNotification(@Body request: ReadNotificationRequest): ReadNotificationResponse

    @GET("/api/hajaty/pages")
    suspend fun getCommonQues(
        @Query("type") type: String,
        @Query("lang") lang: String
    ): CommonQuesResponse

    @GET("/api/hajaty/pages")
    suspend fun getHelpQues(
        @Query("type") type: String,
        @Query("lang") lang: String
    ): HelpQuesResponse

    @GET("/api/orders/list_orders")
    suspend fun getStoreOrders(
        @Query("lang") lang: String,
        @Query("store_id") storeId: Int,
        @Query("limit") limit: Int? = null,
        @Query("status") status: String,
        @Query("page") page: Int? = null
    ): OldOrdersResponse

    @GET("/api/common/settings")
    suspend fun getPhoneNumber(
        @Query("code") code: String,
        @Query("lang") lang: String
    ): PhoneNumberResponse

    @GET("/api/common/settings")
    suspend fun getSocialLinks(
        @Query("code") code: String,
        @Query("lang") lang: String
    ): SocialLinksResponse


    @GET("/api/orders/order_details")
    suspend fun getOrderDetails(
        @Query("order_id") id: Int,
        @Query("lang") lang: String
    ): OrderDetailsResponse


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
        @Part("store[delivery_distance]") deliveryDistance: RequestBody,
        @Part("store[delivery_type]") deliveryType: RequestBody?,
        @Part("store[min_order_price]") minOrderPrice: RequestBody,
        @Part("store[phone_number]") phoneNumber: RequestBody,
        @Part("store[whatsapp]") whatsapp: RequestBody,
        @Part("categories") category: ArrayList<Int>,
        @Part("tags[]") tags: ArrayList<Int>,
        @Part("store[pick_up_from_store]") pickUpFromStore: RequestBody,
        @Part("store[cash_on_delivery]") cashOnDelivery: RequestBody,
        @Part("store[has_delivery]") hasDelivery: RequestBody,

        @Part("store[owner_name]") storeOwnerName: RequestBody,
        @Part("store[bank_name]") storeBankName: RequestBody,
        @Part("store[iban]") storeIban: RequestBody,

        @Part("store_media[]") storeMedia: ArrayList<Int>,
        @Part("delete_media[]") deleteMedia: ArrayList<Int>
    ): UpdateStoreResponse

    @Multipart
    @POST("/api/hajaty/store/update")
    suspend fun updateBankingAccountInfo(
        @Part("store[id]") storeId: RequestBody,
        @Part("store[name]") storeName: RequestBody,
        @Part("store[lat]") storeLat: RequestBody,
        @Part("store[lng]") storeLng: RequestBody,
        @Part("store[address]") address: RequestBody,
        @Part("store[delivery_price]") deliveryPrice: RequestBody,
        @Part("store[delivery_time]") deliveryTime: RequestBody,
        @Part("store[delivery_distance]") deliveryDistance: RequestBody,
        @Part("store[min_order_price]") minOrderPrice: RequestBody,
        @Part("store[phone_number]") phoneNumber: RequestBody,
        @Part("store[whatsapp]") whatsapp: RequestBody,

        @Part("store[owner_name]") storeOwnerName: RequestBody,
        @Part("store[bank_name]") storeBankName: RequestBody,
        @Part("store[iban]") storeIban: RequestBody,
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
        @Query("store_id") storeId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("type") type: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): ReportsTotalResponse

    //reportsDetails
    @GET("/api/hajaty/reports/detailed")
    suspend fun getReportsDetails(
        @Query("store_id") storeId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("type") type: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): ReportsDetailsResponse


    @POST("/api/hajaty/store/tags")
    fun getTags(@Body request: StoreTagsRequest): Call<StoreTagsResponse?>

    @POST("/api/hajaty/store/is_busy")
    fun setMenuBusy(@Body request: MenuBusyRequest): Call<MenuResponse?>

    @POST("/api/hajaty/store/is_closed")
    fun setMenuClosed(@Body request: MenuClosedRequest): Call<MenuResponse?>

    @GET("/api/hajaty/list_units")
    suspend fun getUnitsList(
        @Query("categories[]") category: ArrayList<Int>,
        @Query("locale") locale: String
    ): ListUnitResponse

    @POST("/api/hajaty/clone_products")
    fun setConnectingMain(@Body request: ConnectingMainRequest): Call<ConnectingMainResponse?>

    @GET("/api/hajaty/list_clone_products_requests")
    fun getListConnectingMain(
        @Query("store_id") storeId: Int
    ): Call<ListConnectingMainResponse?>

    @POST("/api/hajaty/accept_clone_products_request")
    fun setAcceptConnectingMain(@Body request: AcceptConnectingMainRequest): Call<AcceptConnectingMainResponse?>

    @Multipart
    @POST("/api/upload")
    suspend fun upload(
        @Part("collection") collection: RequestBody,
        @Part image: MultipartBody.Part
    ): UploadResponse

    // ListStories
    @GET("/api/hajaty/store/list_stories")
    fun getListStories(
        @Query("store_id") storeId: Int
    ): BaseResponse<List<com.brandsin.store.model.ListStoriesResponse>?>

    // DeleteStories
    @POST("/api/hajaty/store/delete_story")
    fun deleteStories(@Body deleteStoryRequest: DeleteStoryRequest?): Call<DeleteStoryResponse?>

    // UploadStories
    @Multipart
    @POST("/api/hajaty/store/upload_story")
    suspend fun uploadStories(
        @Part("store_id") storeId: RequestBody,
        @Part("text") text: RequestBody?,
        @Part("x") x: RequestBody?,
        @Part("y") y: RequestBody?,
        @Part("product_id") productId: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UploadStoryResponse

    @POST("/api/hajaty/store/upload_story")
    suspend fun uploadStoriesWithout(
        @Body uploadStoryWithoutRequest: UploadStoryWithoutRequest
    ): UploadStoryResponse

    //Intro
    @GET("/api/common/introduction_app")
    fun getIntro(@Query("app") app: String): Call<IntroResponse?>

    @POST("api/hajaty/store/categories/create")
    suspend fun addCategory(
        @Query("store_id") storeId: Int,
        @Query("name") nameAr: String,
        @Query("name_en") nameEr: String,
    ): Response<MessageResponse?>

    @GET("api/hajaty/store/categories/list")
    suspend fun getAllCategories(
        @Query("store_id") storeId: Int
    ): Response<CategoriesListResponse?>

    @POST("api/hajaty/store/categories/delete")
    suspend fun deleteCategoryByCategoryId(
        @Query("category_id") categoryId: Int
    ): Response<MessageResponse?>

    @POST("api/hajaty/store/categories/update")
    suspend fun editCategory(
        @Query("category_id") categoryId: Int,
        @Query("name") nameAr: String,
        @Query("name_en") nameEr: String,
    ): Response<MessageResponse?>

    @GET("api/hajaty/offer_and_features")
    suspend fun getAllOffersAndFeatures(): Response<OfferAndFeatureListResponse?>

    @GET("api/hajaty/offer_and_features/{id}")
    suspend fun getOfferAndFeatureById(
        @Path("id") offerAndFeatureId: Int
    ): Response<OfferAndFeatureDetailsResponse>

    @GET("api/stores/get_plans") // {{base}}/api/stores/get_plans?store_id=130
    suspend fun getPlansSubscription(
        @Query("store_id") storeId: Int
    ): Response<SubscriptionListResponse?>

    @POST("api/stores/subscribe") // {{base}}/api/stores/subscribe
    suspend fun addSubscriptionPlan(
        @Query("store_id") storeId: Int,
        @Query("plan_id") planId: Int
    ): Response<SubscriptionListResponse?>

    @GET("api/coupon/{id}") // {{base}}/api/coupon/65
    suspend fun getAllCoupons(
        @Path("id") storeId: Int
    ): Response<CouponListResponse?>

    @DELETE("api/coupon/{id}") // {{base}}/api/coupon/29
    suspend fun deleteCouponById(
        @Path("id") couponId: Int
    ): Response<CouponListResponse?>

    @POST("api/coupon") // {{base}}/api/coupon
    suspend fun createNewCoupon(
        @Query("store_id") storeId: Int,
        @Query("code") code: String,
        @Query("type") type: String, // fixed, percentage
        @Query("value") value: String,
        @Query("start") start: String,
        @Query("expiry") expiry: String,
    ): Response<CreateAndUpdateCouponResponse?>

    @POST("api/coupon/{id}") // {{base}}/api/coupon/28
    suspend fun updateCouponByCouponId(
        @Path("id") couponId: Int,
        @Query("store_id") storeId: Int,
        @Query("code") code: String,
        @Query("type") type: String, // fixed, percentage
        @Query("value") value: String?,
        @Query("start") start: String,
        @Query("expiry") expiry: String,
    ): Response<CreateAndUpdateCouponResponse?>

    @GET("api/refundable_products") // {{base}}/api/refundable_products?store_id=130
    suspend fun getAllRefundableProduct(
        @Query("store_id") storeId: Int,
    ): Response<RefundableProductResponse?>

    @GET("api/refund_details") // {{base}}/api/refund_details?refundable_id=20
    suspend fun getRefundableDetailsByRefundableId(
        @Query("refundable_id") refundableId: Int?,
    ): Response<RefundableDetailsResponse?>

    @POST("api/refundable_products/{id}") // {{base}}/api/refundable_products/20
    suspend fun updateStatusRefundableProduct(
        @Path("id") refundableId: Int?,
        @Query("status") status: String, // [ 'approval', 'rejected']
        @Query("note") note: String?,
    ): Response<MessageResponse?>

    @GET("/api/hajaty/store/list_stories")
    suspend fun getNewListStories(
        @Query("store_id") storeId: Int,
        @Query("locale") locale: String = "ar"
    ): BaseResponse<List<com.brandsin.store.model.ListStoriesResponse>?>

    @GET("/api/common/settings")
    suspend fun getPinStoriesMarketing(
        @Query("code") code: String,
        @Query("lang") lang: String
    ): Response<PinStoriesMarketingResponse>

    @Multipart
    @POST("api/hajaty/marketing_requests")
    suspend fun createMarketingRequests(
        @Part("context") context: RequestBody,
        @Part("type") type: RequestBody,
        @Part("start_date") startDate: RequestBody, // {start_date} 11:00:00
        @Part("end_date") endDate: RequestBody, // {end_date} 11:00:00
        @Part("number_of_shopping_days") numberOfShoppingDays: RequestBody,
        @Part("price") price: RequestBody,
        @Part enImage: MultipartBody.Part?,
        @Part arImage: MultipartBody.Part?,
        ): Response<MessageResponse>

    @FormUrlEncoded
    @POST("api/hajaty/marketing_requests")
    suspend fun createMarketingRequests(
        @Field("context") context: String,
        @Field("type") type: String,
        @Field("story") story: String,  /// Sending as a JSON string
        @Field("start_date") startDate: String, // {start_date} 11:00:00
        @Field("end_date") endDate: String, // {end_date} 11:00:00
        @Field("number_of_shopping_days") numberOfShoppingDays: Int,
        @Field("price") price: Double
    ): Response<MessageResponse>

    @GET("/api/hajaty/store/show")
    suspend fun getStoreProductsList(
        @Query("store_id") storeId: Int,
        @Query("locale") locale: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ListProductsResponse>

    /*
    @POST("api/hajaty/marketing_requests")
    suspend fun createMarketingRequests(
        @Body requestBody: CreateMarketingRequest,
        @Part("products[]") products: ArrayList<Int>,
    ): Response<Void>*/

    @GET("/api/stores/transactions")
    suspend fun getWalletTransactions(
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("store_id") storeId: Int
    ): Response<WalletTransactionsResponse?>
}