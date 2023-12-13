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
            writer.setStyle('height', 'auto', editor.editing.view.document.getRoot());
            writer.setStyle('minHeight', '280px', editor.editing.view.document.getRoot());
        });
        editor.model.document.on('change:data', () => {
            console.log("check editor change")
            // Enable the button when the content changes
            $('#send-edit-note-button').removeClass('disabled');
        });
        editor.model.document.on('change:data', () => {
            console.log("check editor change")
            // Enable the button when the content changes
            $('#send-new-note-button').removeClass('disabled');
        });
        console.log(Array.from(editor.ui.componentFactory.names()));
    })
    .catch(error => {
        // console.error(error);
    });

// // Xử lý fragment identifier nếu có trong URL
// document.addEventListener("DOMContentLoaded", function () {
//     var hash = window.location.hash;
//     if (hash) {
//         var element = document.querySelector(hash);
//         if (element) {
//             var sectionId = hash.substring(1); // Loại bỏ dấu '#'
//             viewSection(sectionId);
//             viewMoreReply(sectionId);
//         }
//     }
// });

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

function viewSection(sectionId) {
    // Ẩn tất cả các phần
    $('.stu__navbar-view-doc-item').each(function () {
        $(this).removeClass('stu__navbar-active');
    });
    $(".section-view").each(function () {
        $(this).css("display", "none");
    })

    if (sectionId !== "note") {
        var selectedSection = $("#link-view-question");
        if (selectedSection.length) {
            selectedSection.addClass('stu__navbar-active');
        }
        var selectedSection = $("#question");
        if (selectedSection.length) {
            selectedSection.css("display", "block");
        }
    } else {
        var selectedSection = $("#link-view-" + sectionId);
        if (selectedSection.length) {
            selectedSection.addClass('stu__navbar-active');
        }
        var selectedSection = $("#" + sectionId);
        if (selectedSection.length) {
            selectedSection.css("display", "block");
        }
    }
}

function sendMessage(qId, type) {
    stompClient.send('/app/private', {}, JSON.stringify({questionId: qId, type: type}));
}

function sendRealtimeQuestion(qId) {
    stompClient.send('/app/question', {}, qId);
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
                sendMessage(data.questionId, "1" , data.questionContent);
                sendRealtimeQuestion(data.questionId);
                // question content
                var html = "<div class=\"stu__question-content-wrapper\" id=\"" + data.questionId + "\">\n" +
                    "                                                <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span> " + data.studentName + " (You)</span></h6>\n" +
                    "                                                <p class=\"stu__question-content question-content\" id=\"question-content-" + data.questionId + "\">" + data.questionContent + "</p>\n";
                    // edit section
                    html+="<div class=\"edit-question-div\" id=\"update-question"+ data.questionId+ "\"style=\"display: none\">\n" +
                    "                                                    <label id=\"update-question-error"+ data.questionId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                    "                                                    <input class=\"update-question\" value=\"" + data.questionContent+ "\" id=\"update-question-content-"+ data.questionId +"\">\n" +
                    "                                                    <button id=\"close-update-question-" + data.questionId+ "\" type=\"button\" title=\"exist\"\n" +
                    "                                                            question-id=\""+ data.questionId+"\" onclick=existFormEditQuestion(\"" +data.questionId+"\")\n" +
                    "                                                            class=\"exist-form-edit-question btn-danger\"><i class=\"fa-solid fa-xmark\"></i> Close</button> " +
                    "                                                            <a type=\"button\" class=\"display-none\" title=\"Sending\" id=\"sending-update-question"+ data.questionId+ "\"><i\n" +
                    "                                                            class=\"fas fa-spinner fa-spin\"></i> Sending...</a>\n" +
                    "                                                    <button type=\"button\" title=\"Edit\" id=\"send-update-question-"+ data.questionId+"\" question-id=\"" + data.questionId + "\"\n" +
                    "                                                            onclick=submitFormEditQuestion(\"" + data.questionId + "\") class=\"btn-save\">" +
                    "                                                    <i class=\"fa-solid fa-paper-plane\"></i> Edit\n" +
                    "                                                    </button>\n" +
                    "                                                    </div>";
                    // date and link
                    html+= "<span class=\"stu__question-date stu__question-content\">" + data.lastModifiedDate +
                    "                                                </span> <a class=\"view-note-link-item  edit-question\" question-id=\""+data.questionId+"\">Edit</a>" +
                    "                                                  | <a class=\"view-note-link-item delete-question\" question-id=\""+ data.questionId +"\" onclick=deleteQuestion(\"" + data.questionId + "\")>Delete</a>" +
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
        $("#send-delete-note-button").css("display", "none");
        console.log(content);
        $.ajax({
            type: 'POST',
            url: '/api/student/document_note/add/' + param,
            data: {'noteContent': content},
            dataType: 'json',
            success: function (data) {
                console.log("success: " + data.noteContent)
                var html = data.noteContent;
                $('#send-edit-note-button').css("display", "inline");
                $("#send-delete-note-button").css("display", "inline");
                $('#sending-new-note').css("display", "none");
                // $('#add-note-form').css("display", "none");
                newEditor.setData(html);
                $('#send-edit-note-button').addClass("disabled");
            },
            error: function (xhr) {
                // Handle errors
                console.log("error")
            }
        });
    }
}
function  submitFormEditNote(param){
    // var content = $('#editor').val();
    var content = newEditor.getData();
    var trimmedString = $.trim(content);
    console.log(newEditor.getData())
    if (trimmedString == '') {
        $('#error-input-new-note').css("display", "block");
    } else {
        $('#send-edit-note-button').css("display", "none");
        $('#sending-new-note').css("display", "block");
        $('#error-input-new-note').css("display", "none");
        $("#send-delete-note-button").css("display", "none");
        console.log(content);
        $.ajax({
            type: 'POST',
            url: '/api/student/document_note/' + param+ '/update',
            data: {'noteContent': content},
            dataType: 'json',
            success: function (data) {
                console.log("success: " + data.noteContent)
                var html = data.noteContent;
                $('#send-edit-note-button').css("display", "inline");
                $('#send-delete-note-button').css("display", "inline");
                $('#sending-new-note').css("display", "none");
                // $('#add-note-form').css("display", "none");
                newEditor.setData(html);
                $('#send-edit-note-button').addClass('disabled');
            },
            error: function (xhr) {
                // Handle errors
                console.log("error")
            }
        });
    }
}

function submitDeleteDocumentNote(param){
    $('#send-delete-note-button').addClass('display-none');
    $('#send-edit-note-button').addClass('display-none');
    $('#sending-delete').css("display", "block");
    var result = window.confirm("Do you want to delete your note?");
    if (result) {
        $.ajax({
            type: 'POST',
            url: '/api/student/document_note/' + param+ '/delete',
            success: function (data) {
                console.log('success-delete-question' + param);
                $('#send-new-note-button').css("display", "inline");
                $('#sending-delete').css("display", "none");
                $('#error-input-new-note').css("display", "none");
                newEditor.setData('');
                $('#send-new-note-button').addClass('disabled');
            },
            error: function (xhr) {
                console.log('error-delete-question')
            }
        });
    }
}

function existFormAddQuestion() {
    $('#form-add-new-question').trigger("reset");
    $('#form-add-new-question').css("display", "none")
}



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
            url: '/api/student/answer/add',
            data: formData,
            dataType: 'json',
            success: function (data) {
                $('#reply-content-form' + param).trigger('reset');
                $('#send-reply-button-' + param).css("display", "inline");
                $('#exist-reply-form-button-' + param).css("display", "inline");
                $('#sending-reply-' + param).css("display", "none");
                $('#reply-form' + param).css("display", "none");
                sendMessage(data.answerId, "3" ,data.answerContent);
                var html = "";
                if (data.studentName == null) {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.lecturerName + "</span></h6>\n" +
                        "                     <p class=\"stu__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"stu__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__like-reply view-question-link-item\" reply-id=\"" + data.answerId + "\"onclick=likeReply(\"" + data.answerId + "\")><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                } else {
                    console.log(data.studentName)
                    // reply content
                    html = "<div class=\"reply-content border-bottom\"  id=\"" + data.answerId + "\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.studentName + "(You)</span></h6>\n" +
                        "                     <p class=\"stu__question-content\" id=\"reply-content-"+ data.answerId +"\"></p>" + data.answerContent + "</p>\n";
                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data.answerId+ "\"style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data.answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <input class=\"update-reply\" value=\"" + data.answerContent + "\" id=\"update-reply-content-"+ data.answerId +"\">\n" +
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
                    html+= "<p class=\"stu__question-content\" ><span class=\"stu__answer-date\" >" + data.lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__edit-reply view-reply-link-item edit-reply\" reply-id=\"" + data.answerId + "\">Edit</a> |" +
                        "                     <a class=\"stu__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data.answerId + "\" onclick=deleteReply(\"" + data.answerId + "\",\"" + data.questionId + "\")>Delete</a>\n" +
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
        url: '/api/student/answers/get/' + param,
        dataType: 'json',
        success: function (data) {
            $("#new-reply-content").val("");
            var html = "";
            for (let i = 0; i < data.length; i++) {
                if (data[i].studentName == null) {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].lecturerName + "</span></h6>\n" +
                        "                     <p class=\"stu__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"stu__question-content\" ><span class=\"stu__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__like-reply view-question-link-item\" reply-id=\"" + data[i].answerId + "\"onclick=likeReply(\"" + data[i].answerId + "\")><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +

                        "                     </div>";
                } else {
                    // content
                    html = "<div class=\"reply-content border-bottom\"  id=\"" + data[i].answerId + "\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].studentName + "(You)</span></h6>\n" +
                        "                     <p class=\"stu__question-content\" id=\"reply-content-"+ data[i].answerId +"\">" + data[i].answerContent + "</p>";
                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data[i].answerId+ "\"style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data[i].answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <textarea class=\"update-reply\"  id=\"update-reply-content-"+ data[i].answerId +"\"></textarea>\n" +
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
                    html+= "<p class=\"stu__question-content\" ><span class=\"stu__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"stu__edit-reply view-reply-link-item edit-reply\" reply-id=\"" + data[i].answerId + "\">Edit</a> |" +
                        "                     <a class=\"stu__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data[i].answerId + "\" onclick=deleteReply(\"" + data[i].answerId + "\",\"" + data[i].questionId + "\")>Delete</a>\n" +
                        "                     </div>";
                }
                $(viewDiv).append(html);
                let modified_val = data[i].answerContent .replace(/"/g, '&quot;')
                $('#update-reply-content-' + data[i].answerId).val(modified_val);
            }
            $(loadingDiv).css("display", "none");

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

function readyStudent() {
    // add scroll to fragment identifier if id exist in URL
    var hash = window.location.hash;
    if (hash) {
        var sectionId = hash.substring(1);// exclude '#'
        if (sectionId !== "note") {
            if ($("#" + sectionId).length > 0) {
                viewMoreReply(sectionId);
            }
        }
        console.log(sectionId);
        viewSection(sectionId);
        $("#" + sectionId).get(0).scrollIntoView();

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
    // edit question
    $("body").on("click", ".edit-question", function () {
        // $(this).next(".edit-question-div").toggle();
        var questionId = $(this).attr("question-id");
        $("#update-question" + questionId).css("display", "block");
        $("#question-content-" + questionId).css("display", "none");
    })
    // edit reply
    $("body").on("click", ".edit-reply", function () {
        // $(this).next(".edit-question-div").toggle();
        var replyId = $(this).attr("reply-id");
        $("#update-reply" + replyId).css("display", "block");
        $("#reply-content-" + replyId).css("display", "none");
    })

};

function submitFormEditQuestion(param) {
    var content = $('#update-question-content-' + param).val();
    var trimmedString = $.trim(content);
    console.log(content);
    if (trimmedString == '') {
        $('#update-question-error' + param).addClass('error');
    } else {
        $('#update-question-error' + param).css("display", "none");
        $('#sending-update-question' + param).css("display", "inline");
        $("#send-update-question-" + param).css("display", 'none');
        $('#close-update-question-' + param).css("display", "none");
        $.ajax({
            type: 'POST',
            url: '/api/student/my_question/' + param + '/update',
            data: {'questionContent': content},
            dataType: 'json',
            success: function (data) {
                console.log(data.questionContent);
                $("#update-question" + param).css("display", "none");
                $("#update-question-content-" + param).val(data.questionContent);
                $("#question-content-" + param).html(data.questionContent);
                $("#question-content-" + param).css("display", "block");
                $('#sending-update-question' + param).css("display", "none");
                $("#send-update-question-" + param).css("display", 'inline');
                $('#close-update-question-' + param).css("display", "inline");
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}

function existFormEditQuestion(param) {
    console.log(param);
    $("#update-question" + param).css("display", "none");
    $("#question-content-" + param).css("display", "block");
    $('#update-question-error' + param).removeClass('error')
}

function deleteQuestion(param) {
    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to delete your question?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'POST',
                url: '/api/student/my_question/' + param + '/delete',
                success: function (data) {
                    Swal.fire(
                        'Deleted!',
                        'Your question has been deleted.',
                        'success'
                    );
                    $("#" + param).html("");
                    $("#" + param).css("display", "none");
                },
                error: function (xhr) {
                    Swal.fire(
                        'Error!',
                        'Failed to delete your question.',
                        'error'
                    );
                }
            });
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
            url: '/api/student/my_question/replies/' + param + '/update',
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
    var result = window.confirm("Do you want to delete your reply?");
    if (result) {
        $.ajax({
            type: 'POST',
            url: '/api/student/my_question/replies/' + param + '/delete',
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


// Note with bôi đen
var isHighlighting = false;

$(document).ready(function () {
    $('#editor').on('mouseup', function (e) {
        var selectedText = $('#editor').getSelection().toString();

        if (selectedText !== '') {
            isHighlighting = true;
            var noteSticker = $('.note-sticker');
            noteSticker.css({top: e.pageY, left: e.pageX});
            noteSticker.fadeIn();

            $('#noteText').val('');
        }
    });

    $('#editor').on('click', function (e) {
        var noteSticker = $('.note-sticker');

        if (noteSticker.is(':visible')) {
            if (!noteSticker.is(e.target) && noteSticker.has(e.target).length === 0 && !isHighlighting) {
                noteSticker.fadeOut(); // Ẩn khung note
            }
        }

        isHighlighting = false;
    });
});

function saveNote() {
    var selectedText = $('#editor').getSelection().toString();
    var noteText = $('#noteText').val();

    $.ajax({
        url: '/',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({text: noteText}),
        success: function (response) {
            if (response.success) {
                var highlightedText = $('<span>').addClass('highlight').text(selectedText);
                ('#editor').getSelection().getRangeAt(0).surroundContents(highlightedText);
            } else {
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });

    $('.note-sticker').fadeOut();
}
