var passChangeForm;


function toggleLike(id) {
    var likeBtn = document.getElementById("likeBtn" + id);
    var likeBtnSpan = document.getElementById("likeBtnSpan" + id);
    var request;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            likeBtnSpan.textContent = this.responseText;
            if (likeBtn.classList.contains("non-liked")) {
                likeBtn.classList.remove("non-liked");
                likeBtn.classList.add("liked");
            } else {
                likeBtn.classList.add("non-liked");
                likeBtn.classList.remove("liked");
            }
        }
    };
    request = "action?act=doLike&postId=" + id;
    xhttp.open("GET", request, true);
    xhttp.send();
}
function toggleRepost(id) {
    var repostBtn = document.getElementById("repostBtn" + id);
    var repostBtnSpan = document.getElementById("repostBtnSpan" + id);
    var request;
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            repostBtnSpan.textContent = this.responseText;
            if (repostBtn.classList.contains("non-reposted")) {
                repostBtn.classList.remove("non-reposted");
                repostBtn.classList.add("reposted");
            } else {
                repostBtn.classList.add("non-reposted");
                repostBtn.classList.remove("reposted");
            }
        }
    };
    request = "action?act=doRepost&postId=" + id;
    xhttp.open("GET", request, true);
    xhttp.send();
}

function removePost(id) {
    var post = document.getElementById("post" + id);
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            post.parentNode.removeChild(post);
        }
    };

    var request = "action?act=doRemove&postId=" + id;
    xhttp.open("GET", request, true);
    xhttp.send();

}
function unsubscribeMenu(id) { // settings/subscriptions
    var xhttp = new XMLHttpRequest();
    var request = "action?act=doSubscribe&subReqId=" + id;
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            if (menuSubscriptions.classList.contains("chosen")) {
                showSubscriptionsMenu();
            }
            if (menuSubscribers.classList.contains("chosen")) {
                showSubscribersMenu();
            }
        }
    };
    xhttp.open("GET", request, true);
    xhttp.send();
    var menuSubscriptions = document.getElementById("menu-subscriptions");
    var menuSubscribers = document.getElementById("menu-subscribers");
}
function toggleSubProfile(id) {
    var xhttp = new XMLHttpRequest();
    var request = "action?act=doSubscribe&subReqId=" + id;
    var button = document.getElementById("subscribeProfileBtn");
    var response;
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            response = JSON.parse(this.responseText);
            if (response["isSubscribed"] == "true") {
                button.classList.remove("bgcolor-blue");
                button.classList.add("bgcolor-dark-blue");
                button.value=response["btnText"];
            }
            if (response["isSubscribed"] == "false") {
                button.classList.remove("bgcolor-dark-blue");
                button.classList.add("bgcolor-blue");
                button.value=response["btnText"];
            }
        }
    };
    xhttp.open("GET", request, true);
    xhttp.send();
}

function toggleSub(id) {
    var xhttp = new XMLHttpRequest();
    var request = "action?act=doSubscribe&subReqId=" + id;
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {

        }
    };
    xhttp.open("GET", request, true);
    xhttp.send();
}

function toggleSubsMenu(e) {
    var caller = e.target;
    var menuSubscriptions = document.getElementById("menu-subscriptions");
    var menuSubscribers = document.getElementById("menu-subscribers");
    if (caller.id == menuSubscribers.id) {
        menuSubscribers.classList.add("chosen");
        menuSubscriptions.classList.remove("chosen");
        showSubscribersMenu()
    }
    if (caller.id == menuSubscriptions.id) {
        menuSubscriptions.classList.add("chosen");
        menuSubscribers.classList.remove("chosen");
        showSubscriptionsMenu()
    }

}
function showSubscriptionsMenu() {
    var container = document.getElementById("subMenu");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            container.innerHTML = this.responseText;
        }
    };
    var request = "subscriptions?act=subscriptions";
    xhttp.open("GET", request, true);
    xhttp.send();
}
function showSubscribersMenu() {
    var container = document.getElementById("subMenu");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            container.innerHTML = this.responseText;
        }
    };
    var request = "subscriptions?act=subscribers";
    xhttp.open("GET", request, true);
    xhttp.send();
}
function doChangeName() {
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            $("#changeNameSubmit").before(this.responseText);
        }
    };
    var request = "settings?act=doChangeName&firstName=" + firstName + "&lastName=" + lastName;
    var token = $("input[name='_csrf']").val();
    if ($("#message").length > 0) {
        $("#message").remove();
    }
    xhttp.open("POST", request, true);
    xhttp.setRequestHeader("X-CSRF-TOKEN",token);
    xhttp.send();
}
function toggleSettingsMenu(e) {
    var caller = e.target;
    var menuProfile = document.getElementById("menu-profile");
    var menuSecurity = document.getElementById("menu-security");
    if (caller.id == menuSecurity.id) {
        menuSecurity.classList.add("chosen");
        menuProfile.classList.remove("chosen");
        showSecurityMenu()
    }
    if (caller.id == menuProfile.id) {
        menuProfile.classList.add("chosen");
        menuSecurity.classList.remove("chosen");
        showProfileMenu()
    }

}
function showProfileMenu() {
    var container = document.getElementById("settings");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            container.innerHTML = this.responseText;
            var arr = container.getElementsByTagName('script');
            for (var n = 0; n < arr.length; n++) {
                eval(arr[n].innerHTML);
            }
        }
    };
    var request = "settings?act=profile";
    xhttp.open("GET", request, true);
    xhttp.send();
}
function showSecurityMenu() {
    var container = document.getElementById("settings");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            container.innerHTML = this.responseText;
            passChangeForm = $("#passChangeForm").on("submit", function (event) {
                event.preventDefault();
                sendForm(passChangeForm);
            });
        }
    };
    var request = "settings?act=security";
    xhttp.open("GET", request, true);
    xhttp.send();
}


function sendForm(form) {
    var data = {
        "previousPassword": $("#previousPassword").val(),
        "newPassword": $("#newPassword").val(),
        "passwordConfirmation": $("#passwordConfirmation").val()
    };
    var token = $("input[name='_csrf']").val();
    if ($("#message").length > 0) {
        $("#message").remove();
    }
    data = JSON.stringify(data);
    $.ajax({
        url: "action?act=doChangePass",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: data,
        async: true,
        cache: false,
        processData: false,
        headers: {
            "X-CSRF-TOKEN": token
        },
        success: function (response) {
            $("#change-name-submit").before(response);
            $("#passChangeForm")[0].reset();
        }
    });
}

function changeLanguage(language) {
    $.ajax({
        url:"settings?lang="+language,
        type:"GET",
        async: true,
        cache: false,
        success: function(response){
            location.reload();
        }
    });
}
function doChangeLanguage() {
    var language;
    var selected = $("#lang-selector").val();
    if(selected=="English"){
        language = "en";
    }
    if (selected == "Русский"){
        language="ru";
    }
    changeLanguage(language);
}