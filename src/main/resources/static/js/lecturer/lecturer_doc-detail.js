function submitFormReplyQuestion(param) {
    var content = $('#new-reply-content-' + param).val();
    var trimmedString = $.trim(content);

    if (trimmedString == '') {
        $('#new-reply-content-error-' + param).css("display", "block");
    } else {
        $('#send-reply-button-' + param).css("display", "none");
        $('#exist-reply-form-button-' + param).css("display", "none");
        $('#sending-reply-' + param).css("display", "inline");
        $('#new-reply-content-error-' + param).css("display", "none");
        var replyForm = 'reply-content-form' + param;
        var formData = $("#" + replyForm).serialize();
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/answer/add',
            data: formData,
            dataType: 'json',
            success: function (data) {
                $('#reply-content-form' + param).trigger('reset');
                $('#send-reply-button-' + param).css("display", "inline");
                $('#exist-reply-form-button-' + param).css("display", "inline");
                $('#sending-reply-' + param).css("display", "none");
                $('#reply-form' + param).css("display", "none");
                var html = "";
                if (data.student == null) {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"likeReply(" + data.answerId + ")\">Edit</a> | <a class=\"lec__delete-reply view-reply-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"deleteReply(" + data.answerId + ")\">Delete</a>\n" +
                        "                     </div>";
                } else {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"likeReply(" + data.answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                }
                $("#list-reply-content-" + param).append(html);
                // change total of answer
                var totalReply = $('#number-reply-' + param).text();
                var intValue = parseInt(totalReply);

                if (!isNaN(intValue)) {
                    // Increment the integer by 1
                    var newValue = intValue + 1;

                    // Set the new value as the text of the span
                    $('#number-reply-' + param).text(newValue);
                } else {
                    $('#number-reply-' + param).text("");
                }
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}

function existFormReplyQuestion(param) {
    $('#reply-content-form' + param).trigger("reset");
    $('#reply-form' + param).css("display", "none")
}

function viewMoreReply(param) {
    var viewDiv = '#list-reply-content-' + param;
    var seeLessDiv = '#see-less-reply-' + param;
    var viewMoreDiv = '#view-more-reply-' + param;
    var loadingDiv = '#loading-reply-' + param;
    $(viewMoreDiv).css("display", "none");
    $(loadingDiv).css("display", "block");
    $.ajax({
        type: 'GET',
        url: '/api/lecturer/answers/get/' + param,
        dataType: 'json',
        success: function (data) {
            $("#new-reply-content").val("");
            var html = "";
            for (let i = 0; i < data.length; i++) {
                if (data[i].student == null) {
                    html += "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"likeReply(" + data[i].answerId + ")\">Edit</a> | <a class=\"lec__delete-reply view-reply-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"deleteReply(" + data[i].answerId + ")\">Delete</a>\n" +
                        "                     </div>";
                } else {
                    html += "<div class=\"reply-content  border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"likeReply(" + data[i].answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                }

            }
            $(loadingDiv).css("display", "none");
            $(viewDiv).html(html);
            $(seeLessDiv).css("display", "block");
        },
        error: function (xhr) {
            // Handle errors
        }
    });
}

function seeLessReply(param) {
    var viewDiv = '#list-reply-content-' + param;
    var seeLessDiv = '#see-less-reply-' + param;
    var viewMoreDiv = '#view-more-reply-' + param;
    $(viewMoreDiv).css("display", "block");
    $(seeLessDiv).css("display", "none");
    $(viewDiv).html('');
}

function showReplyForm(questionId) {
    var questionIdParam = questionId;
    console.log(questionId);
    if (questionId == null) {
        questionIdParam = this.getAttribute('question-id');
    }
    $("#reply-form" + questionIdParam).css("display", "block");
}

$(document).ready(function () {
    // add scroll to fragment identifier if id exist in URL
    var hash = window.location.hash;
    var sectionId = hash.substring(1);// exclude '#'
    if ($("#" + sectionId).length > 0) {
        viewMoreReply(sectionId);
    }
    // var element = document.querySelector("#" + sectionId);
    $("#"+ sectionId).get(0).scrollIntoView();
})
