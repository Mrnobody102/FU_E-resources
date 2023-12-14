var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function disconnect() {
}

const notificationNumber = document.getElementById('notification-number-student');
var notificationCount = notificationNumber ? parseInt(notificationNumber.innerText, 10) : "0";
var numberChange = -1;

function onConnected() {
    stompClient.subscribe('/user/notifications/reply', function (result) {
        numberChange = notificationCount;
        notificationCount++;
        if (notificationNumber) {
            notificationNumber.innerText = notificationCount;
            notificationNumber.style.display = 'flex';
        }
    });

    // stompClient.subscribe(`/user/public`, onMessageReceived);
}

function onError(error) {
    console.log("Lỗi")
}

var notificationDisplay = 0;

function getStudentNotifications(param) {
    console.log(notificationDisplay)
    if (notificationDisplay === 0) {
        if (numberChange === -1 || numberChange !== notificationCount) {
            $('#notificationBoxStudent').show();
            $.ajax({
                type: 'GET',
                url: '/api/notification/' + param,
                dataType: 'json',
                success: function (data) {
                    var ul = $('.header__notify-list');
                    ul.empty();
                    if(data.length === 0) {
                        let noNotification = $('<li id="noNotifyBox" style="color: black; background-color: var(--second-border-color)" class="pt-2 pb-2 border-top border-bottom header__notify-item d-flex justify-content-center">No new notifications</li>');
                        ul.append(noNotification)
                    } else {
                        data.forEach(function (notification) {
                            var li = $('<li class="header__notify-item header__notify-item--seen"></li>');
                            var a = $('<a class="header__notify-link"></a>').attr('href', '/notifications/' + notification.id);
                            var img;
                            if(notification.type === 'LECTURER_REPLY_ON_DOCUMENT') {
                                img = $('<img src="/images/notification_icon/answer.png" alt="" class="header__notify-img">');
                            }
                            if(notification.type === 'LECTURER_UPDATE_COURSE') {
                                img = $('<img src="/images/notification_icon/course.png" alt="" class="header__notify-img">');
                            }
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
                    }
                    notificationDisplay++;
                    numberChange = notificationCount;
                },
                error: function (xhr) {
                    console.log("Nothing")
                }
            });
        } else {
            $('#notificationBoxStudent').show();
            notificationDisplay++;
        }
    } else {
        $('#notificationBoxStudent').hide();
        notificationDisplay = 0;
    }
}

$(document).ready(function () {
    if (notificationCount === parseInt("0")) {
        notificationNumber.style.display = 'none';
    } else {
        notificationNumber.style.display = 'flex';
    }
    console.log("changE" + numberChange)
    connect();
    readyStudent();
});