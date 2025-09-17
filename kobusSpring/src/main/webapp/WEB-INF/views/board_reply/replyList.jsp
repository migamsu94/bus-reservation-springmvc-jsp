<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 댓글 CSS 적용  -->
<style>
.comment-write {
	margin-top: 30px;
	padding: 16px;
	background-color: #f9f9f9;
	border: 1px solid #ccc;
	border-radius: 8px;
}

.comment-write textarea {
	width: 100%;
	height: 100px;
	padding: 10px;
	resize: vertical;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-family: inherit;
	font-size: 14px;
}

.comment-write button {
	margin-top: 10px;
	padding: 8px 16px;
	background-color: #114397;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.comment-write button:hover {
	background-color: #0d356f;
}

.comment-item {
	padding: 10px 12px;
	border-bottom: 1px solid #ddd;
	font-size: 14px;
	position: relative;
}

.comment-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.comment-user {
	font-weight: bold;
	color: #114397;
}

.comment-content {
	margin-left: 5px;
	flex: 1;
	word-break: break-word;
}

.comment-date {
	font-size: 12px;
	color: #888;
	white-space: nowrap;
	margin-left: 10px;
}

.comment-content-edit {
	width: 100%;
	min-height: 40px;
	resize: none;
	overflow: auto;
	font-size: 14px;
	padding: 6px 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	line-height: 1.4;
	box-sizing: border-box;
	background-color: #fff;
	color: #222;
	font-family: inherit;
}
</style>

<c:forEach var="comment" items="${comments}">
    <div class="comment-item" id="comment-${comment.bcmID}">
        <div class="comment-header">
            <span class="comment-user">${comment.kusID}</span>
            <span class="comment-date">
                <fmt:formatDate value="${comment.cmtDate}" pattern="yyyy-MM-dd HH:mm" />
            </span>
        </div>
        <div class="comment-content" id="content-${comment.bcmID}">
            ${comment.content}
        </div>

        <!-- 로그인한 사용자만 수정/삭제 가능 -->
        <c:if test="${sessionScope.auth eq comment.kusID}">
            <div class="comment-actions">
                <button onclick="showEditForm(${comment.bcmID})">수정</button>
                <button onclick="deleteReply(${comment.bcmID})">삭제</button>
            </div>
        </c:if>
    </div>
</c:forEach>





<c:if test="${empty commentList}">
    <p>등록된 댓글이 없습니다.</p>
</c:if>

