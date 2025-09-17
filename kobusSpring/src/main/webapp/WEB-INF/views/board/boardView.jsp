<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세 | 고객지원 | 고속버스통합예매</title>
<link rel="shortcut icon" type="image/x-icon"
	href="/koBus/media/favicon.ico">
<link rel="shortcut icon" type="image/x-icon"
	href="/koBus/images/favicon.ico">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/media/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/media/ui.jqgrid.custom.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/variable/pretendardvariable.css" />

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
/* 필요한 CSS 추가 */
.board-view-container {
	max-width: 800px;
	margin: 40px auto;
	padding: 20px;
	background-color: #fff;
	border: 1px solid #ccc; /* 더 진한 회색 테두리 */
	border-radius: 8px;
	box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.board-view-header {
	border-bottom: 2px solid #114397;
	padding-bottom: 15px;
	margin-bottom: 20px;
}

.board-view-header h2 {
	font-size: 28px;
	color: #2c3e50;
	margin-bottom: 10px;
}

.board-view-meta {
	display: flex;
	justify-content: space-between;
	font-size: 14px;
	color: #777;
}

.board-view-content {
	padding: 20px 0;
	line-height: 1.8;
	color: #333;
	border-bottom: 1px solid #eee;
	margin-bottom: 20px;
	min-height: 150px; /* 내용 영역 최소 높이 */
}

.board-view-actions {
	text-align: right;
	margin-top: 20px;
}

.board-view-actions .btn {
	display: inline-block;
	padding: 10px 20px;
	background-color: #6c757d; /* 회색 계열 */
	color: #fff;
	border: none;
	border-radius: 5px;
	text-decoration: none;
	font-size: 15px;
	cursor: pointer;
	transition: background-color 0.3s ease, transform 0.2s ease;
	margin-left: 10px;
}

.board-view-actions .btn:hover {
	background-color: #5a6268;
	transform: translateY(-2px);
}

.board-view-actions .btn.edit {
	background-color: #007bff; /* 파란색 */
}

.board-view-actions .btn.edit:hover {
	background-color: #0056b3;
}

.board-view-actions .btn.delete {
	background-color: #dc3545; /* 빨간색 */
}

.board-view-actions .btn.delete:hover {
	background-color: #c82333;
}
</style>

</head>
<body class="main KO">


	<div class="content-body customer">
		<div class="container board-view-container">

			<div class="board-view-header">
				<h2>${dto.brdTitle}</h2>
				<div class="board-view-meta">
					<span>작성자: ${dto.userId}</span> <span>작성일: <fmt:formatDate
							value="${dto.brdDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span> <span>조회수:
						${dto.brdViews}</span>
				</div>
			</div>

			<div class="board-view-content">
				<p>${dto.brdContent}</p>
			</div>

			<div class="board-view-actions">
				<a href="${pageContext.request.contextPath}/board/list.do" class="btn">목록</a>
<c:if test="${loginKusID eq dto.kusID}">
    <a href="${pageContext.request.contextPath}/board/edit.do?brdID=${dto.brdID}" class="btn edit">수정</a>

    <form method="post" action="${pageContext.request.contextPath}/board/delete.do" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="brdID" value="${dto.brdID}" />
        <button type="submit" class="btn delete" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
    </form>
</c:if>

			</div>

			<!-- 댓글 목록 출력 -->
<div class="comment-list" style="margin-top: 20px;"></div>
<input type="hidden" name="brdID" value="${dto.brdID}" id="brdID"/>


<!-- 댓글 작성 폼 -->
<div class="comment-write" style="margin-top: 30px;">
<form id="commentForm" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" name="brdID" value="${dto.brdID}" />
    <textarea name="content" rows="3" style="width:100%;" required></textarea>
    <button type="submit">댓글 등록</button>
</form>
</div>


		</div>
	</div>
<script>
$(document).ready(function () {
    const csrfHeader = "${_csrf.headerName}";
    const csrfToken = "${_csrf.token}";

    $('#commentForm').submit(function (e) {
        e.preventDefault();

        const content = $('textarea[name="content"]').val();
        const brdID = $('input[name="brdID"]').val();

        if (!content.trim()) {
            alert("댓글을 입력하세요.");
            return;
        }

        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/replyWrite.do',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: {
                brdID: brdID,
                content: content
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // 🔥 CSRF 토큰 헤더에 추가
            },
            success: function (result) {
                if (result.trim() === 'success') {
                    $('textarea[name="content"]').val('');
                    loadComments();
                } else if (result.trim() === 'nologin') {
                    alert("로그인이 필요합니다.");
                    location.href = '${pageContext.request.contextPath}/user/login.do';
                } else {
                    alert("댓글 등록 실패");
                }
            }
        });
    });

    function loadComments() {
        const brdID = $('input[name="brdID"]').val();
        $.ajax({
            url: '${pageContext.request.contextPath}/replyList.do',
            type: 'GET',
            data: { brdID: brdID },
            success: function (data) {
                $('.comment-list').html(data);
            }
        });
    }

    loadComments();
});
</script>
<script>
$(document).ready(function() {
    const brdID = $("#brdID").val();

    $.ajax({
        url: "${pageContext.request.contextPath}/replyList.do",
        type: "GET",
        data: { brdID: brdID },
        success: function(result) {
            $(".comment-list").html(result);  // 여기서 replyList.jsp 조각이 그대로 들어감
        },
        error: function(xhr) {
            alert("댓글 목록 로딩 실패 (" + xhr.status + ")");
        }
    });
});
</script>
<script>
$(document).on("click", ".btn-edit", function () {
    const item = $(this).closest(".comment-item");
    item.find(".comment-content").hide();
    item.find(".comment-content-edit").show();
    item.find(".btn-edit, .btn-delete").hide();
    item.find(".btn-save, .btn-cancel").show();
});

$(document).on("click", ".btn-cancel", function () {
    const item = $(this).closest(".comment-item");
    item.find(".comment-content-edit").hide();
    item.find(".comment-content").show();
    item.find(".btn-edit, .btn-delete").show();
    item.find(".btn-save, .btn-cancel").hide();
});

$(document).on("click", ".btn-save", function () {
    const item = $(this).closest(".comment-item");
    const bcmID = item.data("bcmid");
    const newContent = item.find(".comment-content-edit").val();

    $.ajax({
        url: "${pageContext.request.contextPath}/replyEdit.do",
        type: "POST",
        data: { bcmID: bcmID, content: newContent },
        success: function () {
            loadComments(); // 수정 후 댓글 다시 불러오기
        },
        error: function () {
            alert("댓글 수정 실패");
        }
    });
});

$(document).on("click", ".btn-delete", function () {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const bcmID = $(this).closest(".comment-item").data("bcmid");

    $.ajax({
        url: "${pageContext.request.contextPath}/replyDelete.do",
        type: "POST",
        data: { bcmID: bcmID },
        success: function () {
            loadComments(); // 삭제 후 댓글 다시 불러오기
        },
        error: function () {
            alert("댓글 삭제 실패");
        }
    });
});
</script>



</body>

</html>