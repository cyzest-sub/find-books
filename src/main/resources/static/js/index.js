$('.message a').click(function(){
    $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});

function register() {

    var password = $('#reg_password').val();

    if (password !== $('#confirm_password').val()) {
        alert("패스워드를 확인해주세요.");
        return false;
    }

    $.ajax({
        url: "/users",
        type: "POST",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'text',
        data: jQuery.param({
            id: $('#reg_id').val(),
            password: $('#reg_password').val()
        }),
        success: function () {
            alert("가입이 완료 되었습니다.");
            location.href = "/";
        },
        error: function (error) {
            console.log(error);
        }
    });

    return false;
}