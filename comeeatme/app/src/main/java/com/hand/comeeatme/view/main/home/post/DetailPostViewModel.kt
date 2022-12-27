package com.hand.comeeatme.view.main.home.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.comment.CommentRepository
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListContent
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class DetailPostViewModel(
    private val postId: Long,
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val commentRepository: CommentRepository,
    private val imageRepository: ImageRepository,
) : BaseViewModel() {
    val detailPostStateLiveData = MutableLiveData<DetailPostState>(DetailPostState.Uninitialized)

    private val hashTagEngToKor = hashMapOf<String, String>(
        "MOODY" to "감성있는",
        "EATING_ALON" to "혼밥",
        "GROUP_MEETING" to "단체모임",
        "DATE" to "데이트",
        "SPECIAL_DAY" to "특별한 날",
        "FRESH_INGREDIENT" to "신선한 재료",
        "SIGNATURE_MENU" to "시그니쳐 메뉴",
        "COST_EFFECTIVENESS" to "가성비",
        "LUXURIOUSNESS" to "고급스러운",
        "STRONG_TASTE" to "자극적인",
        "KINDNESS" to "친절",
        "CLEANLINESS" to "청결",
        "PARKING" to "주차장",
        "PET" to "반려동물 동반",
        "CHILD" to "아이 동반",
        "AROUND_CLOCK" to "24시간"
    )

    private var postWriterMemberId: Long? = null
    private var restaurantId: Long? = null
    private var page: Long = 0
    private var isLast: Boolean = false
    private var contents = arrayListOf<CommentListContent>()

    fun getIsLast(): Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    fun setRestaurantId(restaurantId: Long) {
        this.restaurantId = restaurantId
    }

    fun getRestaurantId() = restaurantId

    fun getHashTagEngToKor(): HashMap<String, String> {
        return hashTagEngToKor
    }

    fun getPostId(): Long {
        return postId
    }

    fun setPostWriterMemberId(memberId: Long) {
        postWriterMemberId = memberId
    }

    fun getPostWriterMemberId(): Long? {
        return postWriterMemberId
    }


    fun getMemberId(): Long {
        return appPreferenceManager.getMemberId()
    }

    fun deletePost() = viewModelScope.launch {
        detailPostStateLiveData.value = DetailPostState.Loading

        val response = postRepository.deletePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.DeletePostSuccess
        } ?:run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "게시글을 삭제하는 도중 오류가 발생했습니다."
            )
        }

    }

    fun modifyComment(commentId: Long, content: String) = viewModelScope.launch {
        val modifyCommentRequest = ModifyCommentRequest(content)
        val response = commentRepository.modifyComment("${appPreferenceManager.getAccessToken()}", postId, commentId, modifyCommentRequest)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.WritingCommentSuccess
        } ?:run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "댓글을 수정하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun deleteComment(commentId: Long) = viewModelScope.launch {
        val response = commentRepository.deleteComment("${appPreferenceManager.getAccessToken()}", postId, commentId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.WritingCommentSuccess
        } ?:run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "댓글을 삭제하는 도중 오류가 발생했습니다."
            )
        }
    }


    fun likePost(postId: Long) = viewModelScope.launch {
        val response = likeRepository.likePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.LikePostSuccess
        } ?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "좋아요 실패"
            )
        }
    }

    fun unLikePost(postId: Long) = viewModelScope.launch {
        val response = likeRepository.unLikePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.UnLikePostSuccess
        } ?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "좋아요 취소 실패"
            )
        }
    }

    fun bookmarkPost(postId: Long) = viewModelScope.launch {
        val response =
            bookmarkRepository.bookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.BookmarkPostSuccess
        } ?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "북마크 실패"
            )
        }
    }

    fun unBookmarkPost(postId: Long) = viewModelScope.launch {
        val response =
            bookmarkRepository.unBookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.UnBookmarkPostSuccess
        } ?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "북마크 취소 실패"
            )
        }

    }

    fun writingComment(parentId: Long?, comment: String) = viewModelScope.launch {
        val commentRequest = WritingCommentRequest(parentId, comment)
        val response = commentRepository.writingComment("${appPreferenceManager.getAccessToken()}", postId, commentRequest)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.WritingCommentSuccess
        }?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "댓글을 작성하는 도중 오류가 발생했습니다."
            )
        }

    }

    fun getDetailPost() = viewModelScope.launch {
        detailPostStateLiveData.value = DetailPostState.Loading

        val response =
            postRepository.getDetailPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            detailPostStateLiveData.value = DetailPostState.Success(
                response = it
            )
        } ?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getCommentList(isRefresh: Boolean) = viewModelScope.launch {
        detailPostStateLiveData.value = DetailPostState.Loading

        if(isRefresh) {
            contents = arrayListOf()
            page = 0
        }

        val response = commentRepository.getCommentList(
            "${appPreferenceManager.getAccessToken()}",
            postId,
            page++,
            10,
        )

        response?.let {
            if(it.data!!.content.isNotEmpty()) {
                contents.addAll(it.data.content)
                detailPostStateLiveData.value = DetailPostState.CommentListSuccess(
                    response = contents
                )
                isLast = false
            } else {
                isLast = true
                detailPostStateLiveData.value = DetailPostState.CommentListSuccess(
                    response = contents
                )
            }


        }?: run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "댓글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getRestaurantImage(restaurantId: Long)  = viewModelScope.launch {
        detailPostStateLiveData.value = DetailPostState.Loading

        val response = imageRepository.getRestaurantImage(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailPostStateLiveData.value = DetailPostState.RestaurantImageSuccess(
                response = it
            )
        }?:run {
            detailPostStateLiveData.value = DetailPostState.Error(
                "음식점 사진을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }


}