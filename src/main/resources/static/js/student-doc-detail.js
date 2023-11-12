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

function submitFormAddQuestion(){
    var formData = $('#form-add-new-question').serialize();
    $.ajax({
        type: 'POST',
        url: '/api/student/question/add',
        data: formData,
        dataType:'json',
        success: function(data) {
            $("#new-question-content").val("");
                $(".form-student-add-doc-new-question").css("display", "none");
                var html = "<div class=\"stu__question-content\">\n" +
                    "                                                <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span> " + data.studentName  + "</span></h6>\n" +
                    "                                                <p class=\"stu__question-content\">" + data.questionContent +"</p>\n" +
                    "                                                    <span class=\"stu__question-date stu__question-content\">" + data.lastModifiedDate + "</span> <a class=\"view-note-link-item \">Edit</a> | <a class=\"view-note-link-item\">Delete</a>\n" +
                    "                                                </div>" +
                    "                                                </div>\n";


                $("#my-questions").prepend(html);
                console.log(data.studentName)
        },
        error: function(xhr) {
            // Handle errors
        }
    });
}

$(document).ready(function () {

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
