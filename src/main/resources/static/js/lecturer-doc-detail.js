function submitFormReplyQuestion(param){
    var replyForm = 'reply-content-form'+ param;
    var formData = $("#"+ replyForm).serialize();
    $.ajax({
        type: 'POST',
        url: '/api/lecturer/answer/add',
        data: formData,
        dataType:'json',
        success: function(data) {
            $("#new-reply-content").val("");
            $(".lec__reply-form").css("display", "none");
            var html = "";
            if(data.student==null){
                html = "<div class=\"reply-content\">\n" +
                    "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>"+ data.lecturerName + "</span></h6>\n" +
                    "                     <p class=\"lec__question-content\">"+ data.answerContent +"</p>\n" +
                    "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                    "                     <a class=\"lec__like-reply view-question-link-item\" question-id=\"" + data.questionId + "\"onclick=\"likeReply(" + data.questionId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                    "                     </div>";
            }else{
                html = "<div class=\"reply-content\">\n" +
                    "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>"+ data.studentName + "</span></h6>\n" +
                    "                     <p class=\"lec__question-content\">"+ data.answerContent +"</p>\n" +
                    "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.lastModifiedDate + "</span> " +
                    "                     <a class=\"lec__like-reply view-question-link-item\" question-id=\"" + data.questionId + "\"onclick=\"likeReply(" + data.answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                    "                     </div>";
            }
            $("#list-reply-content-"+param).prepend(html);
        },
        error: function(xhr) {
            // Handle errors
        }
    });
}
function  existFormReplyQuestion(param){
    $('#reply-content-form'+ param).trigger("reset");
    $('#reply-form'+ param).css("display", "none")
}
function viewMoreReply(param){
    var viewDiv = '#list-reply-content-'+ param;
    var seeLessDiv = '#see-less-reply-'+ param;
    var viewMoreDiv = '#view-more-reply-'+ param;
    var loadingDiv = '#loading-reply-' + param;
    $(viewMoreDiv).css("display", "none");
    $(loadingDiv).css("display", "block");
    $.ajax({
        type: 'GET',
        url: '/api/lecturer/answers/get/'+ param,
        dataType:'json',
        success: function(data) {
            $("#new-reply-content").val("");
            var html = "";
            for (let i = 0; i < data.length; i++) {
                if(data[i].student==null){
                    html += "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>"+ data[i].lecturerName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">"+ data[i].answerContent +"</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" question-id=\"" + data[i].questionId + "\"onclick=\"likeReply(" + data[i].answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                }else{
                    html += "<div class=\"reply-content\">\n" +
                        "                     <h6 class=\"stu__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>"+ data[i].studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">"+ data[i].answerContent +"</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].lastModifiedDate + "</span> " +
                        "                     <a class=\"lec__like-reply view-question-link-item\" question-id=\"" + data[i].questionId + "\"onclick=\"likeReply(" + data[i].answerId + ")\"><i class=\"fa-regular fa-thumbs-up\"></i> Like</a>\n" +
                        "                     </div>";
                }

            }
            $(loadingDiv).css("display", "none");
            $(viewDiv).html(html);
            $(seeLessDiv).css("display", "block");
        },
        error: function(xhr) {
            // Handle errors
        }
    });
}
function seeLessReply(param){
    var viewDiv = '#list-reply-content-'+ param;
    var seeLessDiv = '#see-less-reply-'+ param;
    var viewMoreDiv = '#view-more-reply-'+ param;
    $(viewMoreDiv).css("display", "block");
    $(seeLessDiv).css("display", "none");
    $(viewDiv).html('');
}
function showReplyForm(questionId){
    var questionIdParam = questionId;
    console.log(questionId);
    if(questionId== null){
        questionIdParam = this.getAttribute('question-id');
    }
    $("#reply-form"+questionIdParam).css("display", "block");
}