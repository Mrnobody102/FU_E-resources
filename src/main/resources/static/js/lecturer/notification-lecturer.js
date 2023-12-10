var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function disconnect() {
}

const notificationNumber = document.getElementById('notification-number-lecturer');
var notificationCount = notificationNumber ? parseInt(notificationNumber.innerText, 10) : "0";
var numberChange = -1;

function onConnected() {
    stompClient.subscribe('/user/notifications/private', function (responseData) {
        numberChange = notificationCount;
        notificationCount++;
        if (notificationNumber) {
            notificationNumber.innerText = notificationCount;
            notificationNumber.style.display = 'flex';
        }
    });
    stompClient.subscribe('/user/notifications/question', function (question) {

        question = JSON.parse(question.body); // Parse the data if it's a string
        var html = "<div class='lec__question-content-wrapper' id='" + question.questionId + "'>\n" +
            " <h6 class='lec__question-creater-name'><i class='fa-solid fa-user'></i> <span> " + question.studentName + "</span></h6>\n" +
            " <p class='lec__question-content question-content' id='question-content-" + question.questionId + "'>" + question.questionContent + "</p>\n";

        // reply section
        html += "<div class='edit-reply-div' id='update-reply" + question.answerId + "' style='display: none'>\n" +
            " <label id='update-reply-error" + question.answerId + "' class='display-none'>Please enter something to update.</label>\n" +
            " <input class='update-reply' value='" + question.answerContent + "' id='update-reply-content-" + question.answerId + "'>\n" +
            " <button id='close-update-reply-" + question.answerId + "' type='button' title='exist' " +
            " reply-id='" + question.answerId + "' onclick='existFormEditReply(" + question.answerId + ")' " +
            " class='exist-form-edit-reply btn-danger'><i class='fa-solid fa-xmark'></i> Close</button> " +
            " <a type='button' class='display-none' title='Sending' id='sending-update-reply" + question.answerId + "'><i " +
            " class='fas fa-spinner fa-spin'></i> Sending...</a>\n" +
            " <button type='button' title='Edit' id='send-update-reply-" + question.answerId + "' reply-id='" + question.answerId + "'" +
            " onclick='submitFormEditReply(" + question.answerId + ")' class='btn-save'>" +
            " <i class='fa-solid fa-paper-plane'></i> Edit\n" +
            " </button>\n" +
            " </div>";

        // date and link
        html += "<span class='lec__question-date lec__question-content'>" + question.lastModifiedDate +
            " </span> " +
            " <a class='lec__add-reply view-question-link-item' question-id='" + question.questionId + "' onclick='showReplyForm(this.getAttribute(\"question-id\"))'>Reply</a>" +
            "<p id='view-more-reply-" + question.questionId + "' onclick='viewMoreReply(\"" + question.questionId + "\")' " +
            " class='view-question-reply lec__question-content' style='display:none;'>View all <span id='number-reply-" + question.questionId + "'>" +
            question.totalAnswer + "</span> reply</p>" +
            "<p style='display:none;' id='see-less-reply-" + question.questionId + "' onclick='seeLessReply(\"" + question.questionId + "\")' " +
            " class='view-question-reply lec__question-content' th:classappend='${question.answers.length == 0 ? \"display-none\" : \"\"}'>See less</p>" +
            "<p style='display:none;' id='loading-reply-" + question.questionId + "' class='view-question-reply lec__question-content'><i " +
            " class='fas fa-spinner fa-spin'></i> Loading more reply...</p>" +
            "<div class='reply-content' id='list-reply-content-" + question.questionId + "'></div>" +
            "<div class='lec__reply-form' id='reply-form" + question.questionId + "'>" +
            " <form class='form-lecturer-add-doc-new-reply justify-content-between' method='post' role='form' id='reply-content-form" + question.questionId + "'>" +
            " <div>" +
            " <input type='text' name='docId' value='" + question.documentId + "' readonly hidden>" +
            " <input type='text' name='quesId' value='" + question.questionId + "' readonly hidden>" +
            " <label id='new-reply-content-error-" + question.questionId + "' class='lecturer_input-label error-input'>Content require!</label>" +
            " <div class='form-content'>" +
            " <label for='new-reply-content-" + question.questionId + "' class='lec_input-label-reply'>Enter answer:</label>" +
            " <textarea id='new-reply-content-" + question.questionId + "' class='input-reply-content' name='answer' required></textarea>" +
            " </div>" +
            " </div>" +
            " <div class='submit-reply-button'>" +
            " <button type='button' title='exist' id='exist-reply-form-button-" + question.questionId + "' onclick='existFormReplyQuestion(\"" + question.questionId + "\")' class='btn-danger'>" +
            " <i class='fa-solid fa-xmark'></i> Close" +
            " </button>" +
            " <a type='button' title='Sending' style='display:none;' id='sending-reply-" + question.questionId + "'><i class='fas fa-spinner fa-spin'></i> Sending...</a>" +
            " <button type='button' title='Send' id='send-reply-button-" + question.questionId + "' onclick='submitFormReplyQuestion(\"" + question.questionId + "\")' class='btn-save'>" +
            " <i class='fa-solid fa-paper-plane'></i>Send</button>" +
            " </div>" +
            " </form>" +
            " </div>" + " </div>\n";
        ;

        $("#lecturer-questions").prepend(html);
    });


    // stompClient.subscribe(`/user/public`, onMessageReceived);
}

function onError(error) {
    console.log("Lỗi")
}

var notificationDisplay = 0;

function getNotifications(param) {
    if (notificationDisplay === 0) {
        if (numberChange === -1 || numberChange !== notificationCount) {
            $('#notificationBoxLecturer').show();
            $.ajax({
                type: 'GET',
                url: '/api/notification/' + param,
                dataType: 'json',
                success: function (data) {
                    var ul = $('.header__notify-list');
                    ul.empty();
                    data.forEach(function (notification) {
                        var li = $('<li class="header__notify-item header__notify-item--seen"></li>');
                        var a = $('<a class="header__notify-link"></a>').attr('href', '/notifications/' + notification.id);
                        var img = $('<img src="/images/notification_icon/question.png" alt="" class="header__notify-img">');
                        var info = $('<div class="header__notify-info"></div>');
                        var name = $('<span class="header__notify-name"></span>').text(notification.notificationContent);
                        var description = $('<span class="header__notify-description"></span>').text(notification.lastModifiedDate);

                        info.append(name);
                        info.append(description);
                        a.append(img);
                        a.append(info);
                        li.append(a);
                        ul.append(li);
                    });

                    notificationDisplay++;
                    numberChange = notificationCount;
                },
                error: function (xhr) {
                    // Handle errors
                }
            });
        } else {
            $('#notificationBoxLecturer').show();
            notificationDisplay++;
        }
    } else {
        $('#notificationBoxLecturer').hide();
        notificationDisplay = 0;
    }
}

$(document).ready(function () {
    if (notificationCount === parseInt("0")) {
        notificationNumber.style.display = 'none';
    } else {
        notificationNumber.style.display = 'flex';
    }
    connect()
});