
var stompClient = null;
function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

const notificationNumber = document.getElementById('notification-number-lecturer');
var notificationCount = notificationNumber ? parseInt(notificationNumber.innerText, 10) : "0";

function onConnected() {
    stompClient.subscribe('/user/notifications/private', function (result) {
        notificationCount++;
        if(notificationNumber){
            notificationNumber.innerText = notificationCount;
        }
        console.log(result.body)
    });

    // stompClient.subscribe(`/user/public`, onMessageReceived);
}

function onError(error) {
    console.log("Lỗi")
}

function sendMessage(qId, content) {
    stompClient.send('/app/private', {}, JSON.stringify({id: qId, type: "1", content: content}));
    console.log(notificationCount)
}
var notificationDisplay = 0;

function getNotifications(param) {
    if (notificationDisplay === 0) {
        $('#notificationBox').show();
        $.ajax({
            type: 'GET',
            url: '/api/notification/' + param,
            dataType: 'json',
            success: function (data) {
                var ul = $('.header__notify-list');
                ul.empty();
                data.forEach(function(notification) {
                    var li = $('<li class="header__notify-item header__notify-item--seen"></li>');
                    var a = $('<a class="header__notify-link"></a>').attr('href', notification.link);
                    var img = $('<img src="" alt="" class="header__notify-img">');
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
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    } else {
        $('#notificationBox').hide();
        notificationDisplay = 0;
    }
}
$(document).ready(function () {
    connect()
});