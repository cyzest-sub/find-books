$(function() {

    $('#login-form-link').click(function(e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

    $('#register-form-link').click(function(e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

});

function login() {

    var id = $.trim($("#id").val());
    var password = $.trim($("#password").val());

    if (id === "") {
        $("#id").focus();
        alert("아이디를 입력하세요.");
        return false;
    }

    if (password === "") {
        $("#password").focus();
        alert("패스워드를 입력하세요.");
        return false;
    }

    $.ajax({
        url: "/login",
        type: "POST",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'text',
        data: jQuery.param({
            username: id,
            password: password
        }),
        success: function () {
            window.location.href = "/";
        },
        error: function (error) {
            alert("이메일이 없거나 비밀번호가 틀립니다.");
            return false;
        }
    });

    return false;
}

function register() {

    var id = $('#reg_id').val();
    var password = $('#reg_password').val();
    var confirmPassword = $('#confirm-password').val();

    if (id === '') {
        $('#reg_id').focus();
        alert("아이디를 입력하세요.");
        return false;
    }

    if (password === '') {
        $('#reg_password').focus();
        alert("패스워드를 입력하세요.");
        return false;
    }

    if (confirmPassword === '' || password !== confirmPassword) {
        $('#confirm-password').focus();
        alert("패스워드 확인을 해주세요.");
        return false;
    }

    $.ajax({
        url: "/users",
        type: "POST",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'text',
        data: jQuery.param({
            id: id,
            password: password
        }),
        success: function () {
            window.location.href = "/";
        },
        error: function (error) {
            alert(error);
            return false;
        }
    });

    return false;
}