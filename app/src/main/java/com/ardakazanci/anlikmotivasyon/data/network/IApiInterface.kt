package com.ardakazanci.anlikmotivasyon.data.network

import com.ardakazanci.anlikmotivasyon.data.model.ContentResponse
import com.ardakazanci.anlikmotivasyon.data.model.DataModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface IApiInterface {

    @POST("/user/signUp")
    fun requestSignUp(@Body bodyData: DataModel.SignUpRequestModel): Deferred<Response<DataModel.SignUpResponseModel>>

    @POST("/user/login")
    fun requestSignIn(@Body bodyData: DataModel.SignInRequestModel): Deferred<Response<DataModel.SignInResponseModel>>


    @GET("/user/{userid}")
    fun requestProfileInfo(
        @Path("userid") userid: String,
        @Header("Authorization") auth: String
    ): Deferred<Response<DataModel.ProfileInfoResponseModel>>


    @GET("/followoperation/followerlist/{userid}")
    fun requestFollowerListInfo(
        @Path("userid") userid: String
    ): Deferred<Response<DataModel.FollowerListResponse>>

    @GET("/followoperation/followedlist/{userid}")
    fun requestFollowedListInfo(
        @Path("userid") userid: String
    ): Deferred<Response<DataModel.FollowedListResponse>>


    // UnFollow işlemi gerçekleştirilecek
    // unfollow a gönderilen 2 adet ID vardır , param olarak giriş yapan kullanıcı
    // body olarak takip edilen kullanıcı

    @HTTP(method = "DELETE", path = "/followoperation/unfollow/{userid}", hasBody = true)
    fun requesUnFollow(
        @Path("userid") userid: String,
        @Body bodyData: DataModel.UnFollowRequestModel
    ): Deferred<Response<DataModel.UnFollowResponseModel>>

    @PUT("/followoperation/follow/{otheruserid}")
    fun requestFollow(
        @Path("otheruserid") otheruserid: String,
        @Body bodyData: DataModel.FollowRequestModel
    ): Deferred<Response<DataModel.FollowResponseModel>>

    @POST("/contentoperation/add")
    fun requestContentAdd(
        @Body bodyData: DataModel.requestBodyContentShare, // Body Data
        @Header("Authorization") auth: String // Token
    ): Deferred<Response<DataModel.responseContentShare>>

    // CONTENT - HOME FRAGMENT FETCH DATA
    @GET("/contentoperation/getAllShared")
    suspend fun fetchContents(
        @Query("limit") loadSize: Int = 10,
        @Query("after") after: Int? = null,
        @Query("before") before: Int? = null
    ): Response<ContentResponse>


    // GET USER SHARED CONTENT
    @GET("/contentoperation/userShared/{userid}")
    fun fetchUserSharedContent(
        @Path("userid") userid: String
    ): Deferred<Response<DataModel.UserSharedContentResponse>>


    @PUT("/contentoperation/like/{contentid}")
    fun requestLike(
        @Path("contentid") contentid: String
    ): Deferred<Response<DataModel.LikeResponse>>

    @PUT("/contentoperation/dislike/{contentid}")
    fun requestDislike(
        @Path("contentid") contentid: String
    ): Deferred<Response<DataModel.DislikeResponse>>


    // Mesaj gönderme arayüzü.
    @POST("/messageoperation/send")
    fun requestMessageSend(
        @Body bodyData: DataModel.MessageSendRequest
    ): Deferred<Response<DataModel.MessageSendResponse>>


    @POST("/messageoperation/list")
    fun requestMessageGetList(
        @Body bodyData: DataModel.MessageGetRequestContainer
    ): Deferred<Response<DataModel.MessageGetResponse>>


    // http://localhost:3000/messageoperation/conversationlist/5e26bc3645cdeb0efb0629d2
    @GET("/messageoperation/conversationlist/{currentId}")
    fun requestConversationList(
        @Path("currentId") currentId: String
    ): Deferred<Response<DataModel.MessageConversationResponseModel>>

}


