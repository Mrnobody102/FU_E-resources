var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
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

        const questionContentWrapper = document.createElement('div');
        questionContentWrapper.innerHTML = `
      <h6 class="lec__question-creater-name"><i class="fa-solid fa-user"></i> <span>${responseData.name}</span></h6>
      <p class="lec__question-content question-content">${responseData.questionContent}</p>
      <p class="lec__question-content"><span class="lec__answer-date">${responseData.lastModifiedDate}</span> <a class="lec__add-reply view-question-link-item" question-id="${responseData.id}" onclick="showReplyForm(this.getAttribute('question-id'))">Reply</a></p>
      <p id="view-more-reply-${responseData.id}" question-id="${responseData.id}" onclick="viewMoreReply(this.getAttribute('question-id'))" class="view-question-reply lec__question-content">View all <span id="number-reply-${responseData.id}">${responseData.replyCount}</span> reply</p>
      <p style="display:none;" id="see-less-reply-${responseData.id}" question-id="${responseData.id}" onclick="seeLessReply(this.getAttribute('question-id'))" class="view-question-reply lec__question-content">See less</p>
      <p style="display:none;" id="loading-reply-${responseData.id}" question-id="${responseData.id}" class="view-question-reply lec__question-content"><i class="fas fa-spinner fa-spin"></i> Loading more reply...</p>
      <div class="reply-content" id="list-reply-content-${responseData.id}"></div>
      <div class="lec__reply-form" id="reply-form${responseData.id}">
          <form class="form-lecturer-add-doc-new-reply justify-content-between" method="post" role="form" id="reply-content-form${responseData.id}">
              <div class="">
                  <input type="text" name="docId" value="${responseData.docId}" readonly="" hidden="">
                  <input type="text" name="quesId" value="${responseData.id}" readonly="" hidden="">
                  <label id="new-reply-content-error-${responseData.id}" class="lecturer_input-label error-input">Content require!</label>
                  <div class="form-content">
                      <label for="new-reply-content-${responseData.id}" class="lec_input-label-reply">Enter answer:</label>
                      <textarea id="new-reply-content-${responseData.id}" class="input-reply-content" required="" name="answer"></textarea>
                  </div>
              </div>
              <div class="submit-reply-button">
                  <button type="button" title="exist" id="exist-reply-form-button-${responseData.id}" question-id="${responseData.id}" onclick="existFormReplyQuestion(this.getAttribute('question-id'))" class="btn-danger"><i class="fa-solid fa-xmark"></i> Close</button>
                  <a type="button" title="Sending" style="display:none;" id="sending-reply-${responseData.id}" question-id="${responseData.id}"><i class="fas fa-spinner fa-spin"></i> Sending...</a>
                  <button type="button" title="Send" id="send-reply-button-${responseData.id}" question-id="${responseData.id}" onclick="submitFormReplyQuestion(this.getAttribute('question-id'))" class="btn-save"><i class="fa-solid fa-paper-plane"></i> Send</button>
              </div>
          </form>
      </div>
    `;

        const questionInDocContent = document.querySelector('.lec__question-in-doc-content');
        questionInDocContent.insertBefore(questionContentWrapper, questionInDocContent.firstChild);
    });
    console.log("number" + notificationCount)
    console.log(notificationNumber)

    // stompClient.subscribe(`/user/public`, onMessageReceived);
}

function onError(error) {
    console.log("Lỗi")
}

var notificationDisplay = 0;

function getNotifications(param) {
    if (notificationDisplay === 0) {
        console.log(numberChange)
        console.log(notificationCount)
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
                        var a = $('<a class="header__notify-link"></a>').attr('href', notification.link);
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