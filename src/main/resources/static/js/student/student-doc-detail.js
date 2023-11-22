let newEditor;
// CK editor
ClassicEditor
    .create(document.querySelector('#editor'), {
        ckfinder: {
            uploadUrl: '/ckfinder/connector/?command=QuickUpload&type=Files&responseType=json'
        },
    })
    .then(editor => {
        newEditor = editor;
        editor.editing.view.change(writer => {
            writer.setStyle('height', '200px', editor.editing.view.document.getRoot());
        });
        console.log(Array.from(editor.ui.componentFactory.names()));
    })
    .catch(error => {
        console.error(error);
    });

// Xử lý fragment identifier nếu có trong URL
document.addEventListener("DOMContentLoaded", function () {
    var hash = window.location.hash;
    if (hash) {
        var element = document.querySelector(hash);
        if (element) {
            var sectionId = hash.substring(1); // Loại bỏ dấu '#'
            showSection(sectionId);

        }
    }
});


function viewQuestion() {
    $("#note").css("display", "none");
    $("#question").css("display", "block");
    $("#link-view-notes").removeClass("stu__navbar-active")
    $("#link-view-questions").addClass("stu__navbar-active");
}

function viewNote() {
    $("#note").css("display", "block");
    $("#question").css("display", "none");
    $("#link-view-questions").removeClass("stu__navbar-active")
    $("#link-view-notes").addClass("stu__navbar-active");
}
function viewSection(sectionId){
    // Ẩn tất cả các phần
    $('.stu__navbar-view-doc-item').each(function() {
        $(this).removeClass('stu__navbar-active');
    });
    $(".section-view").each(function (){
        $(this).css("display", "none");
    })

    var selectedSection = $("#link-view-" + sectionId);
    if (selectedSection.length) {
        selectedSection.addClass('stu__navbar-active');
    }
    var selectedSection = $("#" + sectionId);
    if (selectedSection.length) {
        selectedSection.css("display", "block");
    }
}

function submitFormAddQuestion(param) {

    var content = $('#new-question-content').val();
    var trimmedString = $.trim(content);

    if (trimmedString == '') {
        $('#error-input-new-question').css("display", "block");
    } else {
        $('#send-new-question-button').css("display", "none");
        $('#exist-new-question-form-button').css("display", "none");
        $('#sending-new-question').css("display", "inline");
        $('#error-input-new-question').css("display", "none");
        var formData = $('#form-add-new-question').serialize();
        // console.log(formData)
        $.ajax({
            type: 'POST',
            url: '/api/student/question/add',
            data: formData,
            dataType: 'json',
            success: function (data) {
                $("#form-add-new-question").trigger("reset");
                $('#send-new-question-button').css("display", "inline");
                $('#exist-new-question-form-button').css("display", "inline");
                $('#sending-new-question').css("display", "none");
                $(".form-student-add-doc-new-question").css("display", "none");
                var html = "<div class=\"stu__question-content\">\n" +
                    "                                                <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span> " + data.studentName + "(You)</span></h6>\n" +
                    "                                                <p class=\"stu__question-content\">" + data.questionContent + "</p>\n" +
                    "                                                <span class=\"stu__question-date stu__question-content\">" + data.lastModifiedDate + "</span> <a class=\"view-note-link-item \">Edit</a> | <a class=\"view-note-link-item\">Delete</a>" +
                    "                                                </div>\n";


                $("#my-questions").prepend(html);
                console.log(data.studentName)
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}

function submitFormAddNote(param) {
    // var content = $('#editor').val();
    var content = newEditor.getData();
    var trimmedString = $.trim(content);
    console.log(newEditor.getData())
    if (trimmedString == '') {
        $('#error-input-new-note').css("display", "block");
    } else {
        $('#send-new-note-button').css("display", "none");
        $('#sending-new-note').css("display", "block");
        $('#error-input-new-note').css("display", "none");
        // newEditor.updateElement();
        //
        // var data = $('#myForm').serializeArray();
        // var formData = $('#add-note-form').serialize();
        // formData.set('noteContent', editorContent);
        console.log(content);
        $.ajax({
            type: 'POST',
            url: '/api/student/document_note/add/'+param,
            data: content,
            dataType: 'application/text',
            success: function (data) {
                console.log("success"+data)
                // var html = data.noteContent;
                $("#new-note-content").html(html);
                $('#stu__note-in-doc-content-new').css("display", "block");
                $('#sending-new-note').css("display", "none");
                $('#add-note-form').css("display", "none");

            },
            error: function (xhr) {
                // Handle errors
                console.log("error")
            }
        });
    }
}
function existFormAddQuestion(){
    $('#form-add-new-question').trigger("reset");
    $('#form-add-new-question').css("display", "none")
}

function submitFormReplyQuestion(param) {
    var content = $('#new-reply-content-' + param).val();
    var trimmedString = $.trim(content);

    if (trimmedString == '') {
        $('#new-reply-content-error-' + param).css("display","block");
    } else {
        $('#send-reply-button-'+ param).css("display", "none");
        $('#exist-reply-form-button-'+ param).css("display", "none");
        $('#sending-reply-'+ param).css("display", "inline");
        $('#new-reply-content-error-' + param).css("display","none");
        var replyForm = 'reply-content-form' + param;
        var formData = $("#" + replyForm).serialize();
        $.ajax({
            type: 'POST',
            url: '/api/student/answer/add',
            data: formData,
            dataType: 'json',
            success: function (data) {
                $('#reply-content-form' + param).trigger('reset');
                $('#send-reply-button-'+ param).css("display", "inline");
                $('#exist-reply-form-button-'+ param).css("display", "inline");
                $('#sending-reply-'+ param).css("display", "none");
                $('#reply-form' + param).css("display", "none");
                var html = "";
                if (data.studentName == null) {
                    html = "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.lecturerName + "</span></h6>\n" +
                        "                     <p class=\"stu__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"stu__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__like-reply view-question-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"likeReply(" + data.answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                } else {
                    html = "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.studentName + "(You)</span></h6>\n" +
                        "                     <p class=\"stu__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"stu__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__edit-reply view-reply-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"likeReply(" + data.answerId + ")\">Edit</a> | <a class=\"stu__delete-reply view-reply-link-item\" reply-id=\"" + data.answerId + "\"onclick=\"deleteReply(" + data.answerId + ")\">Delete</a>\n" +
                        "                     </div>";
                }
                $("#list-reply-content-" + param).append(html);
                // change total of answer
                var totalReply = $('#number-reply-'+param).text();
                var intValue = parseInt(totalReply);

                if (!isNaN(intValue)) {
                    // Increment the integer by 1
                    var newValue = intValue + 1;

                    // Set the new value as the text of the span
                    $('#number-reply-'+param).text(newValue);
                } else {
                    $('#number-reply-'+param).text("");
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
        url: '/api/student/answers/get/' + param,
        dataType: 'json',
        success: function (data) {
            $("#new-reply-content").val("");
            var html = "";
            for (let i = 0; i < data.length; i++) {
                if (data[i].studentName == null) {
                    html += "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].lecturerName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__like-reply view-question-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"likeReply(" + data[i].answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +

                        "                     </div>";
                } else {
                    html += "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].studentName + "(You)</span></h6>\n" +
                        "                     <p class=\"stu__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"stu__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__edit-reply view-reply-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"likeReply(" + data[i].answerId + ")\">Edit</a> | <a class=\"stu__delete-reply view-reply-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=\"deleteReply(" + data[i].answerId + ")\">Delete</a>\n" +
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
    if (hash) {
        var element = document.querySelector(hash);
        if(element){
            var sectionId = hash.substring(1); // exclude '#'
            viewSection(sectionId);
            element.scrollIntoView();
        }
    }

    $("body").on("click", ".add-note", function () {
        var contextPath = $(this).attr("contextPath");
        var id = $(this).attr("docId");
        alert(contextPath + "/student/document/note/add");

        $.get({
            dataType: 'JSON',
            url: contextPath + "/student/document/note/add/" + id,
            success: function (responseData) {
                /*$(".stu__note-content").html(responseData.content);*/
                alert(responseData.message);

            },
            error: function (errorData) {
                $(".card-body").html(errorData);
            }
        })
    });
    $("body").on("click", ".add-doc-question", function () {
        $(".form-student-add-doc-new-question").css("display", "block");
    });

    $("body").on("click", ".add-doc-question", function () {
        $(".form-student-add-doc-new-question").css("display", "block");
    });

});
