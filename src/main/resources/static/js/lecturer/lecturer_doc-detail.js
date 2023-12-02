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
                if (data.studentName == null) {
                    html = "<div class=\"reply-content border-bottom\"  id=\"" + data.answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\" id=\"reply-content-"+ data.answerId +"\">" + data.answerContent + "</p>";
                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data.answerId+ "\"style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data.answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <input class=\"update-reply\" value=\"" + data.answerContent+ "\" id=\"update-reply-content-"+ data.answerId +"\">\n" +
                        "                                                    <button id=\"close-update-reply-" + data.answerId+ "\" type=\"button\" title=\"exist\"\n" +
                        "                                                            reply-id=\""+ data.answerId+"\" onclick=existFormEditReply(\"" +data.answerId+"\")\n" +
                        "                                                            class=\"exist-form-edit-reply btn-danger\"><i class=\"fa-solid fa-xmark\"></i> Close</button> " +
                        "                                                            <a type=\"button\" class=\"display-none\" title=\"Sending\" id=\"sending-update-reply"+ data.answerId+ "\"><i\n" +
                        "                                                            class=\"fas fa-spinner fa-spin\"></i> Sending...</a>\n" +
                        "                                                    <button type=\"button\" title=\"Edit\" id=\"send-update-reply-"+ data.answerId+"\" reply-id=\"" + data.answerId + "\"\n" +
                        "                                                            onclick=submitFormEditReply(\"" + data.answerId + "\") class=\"btn-save\">" +
                        "                                                    <i class=\"fa-solid fa-paper-plane\"></i> Edit\n" +
                        "                                                    </button>\n" +
                        "                                                    </div>";
                    // date and link
                    html+="<p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item  edit-reply\" reply-id=\"" + data.answerId + "\">Edit</a> | " +
                        "                     <a class=\"lec__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data.answerId + "\" onclick=deleteReply(\"" + data.answerId + "\",\"" + data.questionId + "\")>Delete</a>\n" +
                        "                     </div>";
                } else {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" reply-id=\"" + data.answerId + "\" onclick=likeReply(\"" + data.answerId + "\")><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
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
function likeReply(){}
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
                if (data[i].studentName == null) {
                    // content
                    html += "<div class=\"reply-content border-bottom\" id=\"" + data[i].answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\"  id=\"reply-content-"+ data[i].answerId +"\">" + data[i].answerContent + "</p>";

                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data[i].answerId+ "\"style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data[i].answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <input class=\"update-reply\" value=\"" + data[i].answerContent+ "\" id=\"update-reply-content-"+ data[i].answerId +"\">\n" +
                        "                                                    <button id=\"close-update-reply-" + data[i].answerId+ "\" type=\"button\" title=\"exist\"\n" +
                        "                                                            reply-id=\""+ data[i].answerId+"\" onclick=existFormEditReply(\"" +data[i].answerId+"\")\n" +
                        "                                                            class=\"exist-form-edit-reply btn-danger\"><i class=\"fa-solid fa-xmark\"></i> Close</button> " +
                        "                                                            <a type=\"button\" class=\"display-none\" title=\"Sending\" id=\"sending-update-reply"+ data[i].answerId+ "\"><i\n" +
                        "                                                            class=\"fas fa-spinner fa-spin\"></i> Sending...</a>\n" +
                        "                                                    <button type=\"button\" title=\"Edit\" id=\"send-update-reply-"+ data[i].answerId+"\" reply-id=\"" + data[i].answerId + "\"\n" +
                        "                                                            onclick=submitFormEditReply(\"" + data[i].answerId + "\") class=\"btn-save\">" +
                        "                                                    <i class=\"fa-solid fa-paper-plane\"></i> Edit\n" +
                        "                                                    </button>\n" +
                        "                                                    </div>";
                    // date and link
                    html+="<p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item  edit-reply\" reply-id=\"" + data[i].answerId + "\">Edit</a> | " +
                        "                     <a class=\"lec__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data[i].answerId + "\" onclick=deleteReply(\"" + data[i].answerId + "\",\"" + data[i].questionId + "\")>Delete</a>\n" +
                        "                     </div>";
                } else {
                    html += "<div class=\"reply-content  border-bottom\"  id=\"" + data[i].answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=likeReply(\"" + data[i].answerId + "\")><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
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


function submitFormEditReply(param) {
    console.log(param)
    var content = $('#update-reply-content-' + param).val();
    var trimmedString = $.trim(content);
    console.log(content);
    if (trimmedString == '') {
        $('#update-reply-error' + param).addClass('error');
    } else {
        $('#update-reply-error' + param).css("display", "none");
        $('#sending-update-reply' + param).css("display", "inline");
        $("#send-update-reply-" + param).css("display", 'none');
        $('#close-update-reply-' + param).css("display", "none");
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/my_question/replies/' + param + '/update',
            data: {'answerContent': content},
            dataType: 'json',
            success: function (data) {
                console.log(data.answerContent);
                $("#update-reply" + param).css("display", "none");
                $("#update-reply-content-" + param).val(data.answerContent);
                $("#reply-content-" + param).html(data.answerContent);
                $("#reply-content-" + param).css("display", "block");
                $('#sending-update-reply' + param).css("display", "none");
                $("#send-update-reply-" + param).css("display", 'inline');
                $('#close-update-reply-' + param).css("display", "inline");
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}

function existFormEditReply(param) {
    console.log(param);
    $("#update-reply" + param).css("display", "none");
    $("#reply-content-" + param).css("display", "block");
    $('#update-reply-error' + param).removeClass('error')
}

function deleteReply(param, param2) {
    console.log(param)
    var result = window.confirm("Do you want to delete your reply?");
    if (result) {
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/my_question/replies/' + param + '/delete',
            success: function (data) {
                console.log('success-delete-reply' + param);
                $("#" + param).html("");
                $("#" + param).css("display", "none");
                // change total of answer
                var totalReply = $('#number-reply-' + param2).text();
                var intValue = parseInt(totalReply);

                if (!isNaN(intValue)) {
                    // Increment the integer by 1
                    var newValue = intValue - 1;

                    // Set the new value as the text of the span
                    $('#number-reply-' + param2).text(newValue);
                } else {
                    $('#number-reply-' + param2).text("");
                }
            },
            error: function (xhr) {
                console.log('error-delete-reply')
            }
        });
    }
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

    // edit reply
    $("body").on("click", ".edit-reply", function () {
        // $(this).next(".edit-question-div").toggle();
        var replyId = $(this).attr("reply-id");
        $("#update-reply" + replyId).css("display", "block");
        $("#reply-content-" + replyId).css("display", "none");
    })
})
