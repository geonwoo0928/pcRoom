// 회원정보 수정
function check2() {
    if (document.getElementById("userId").value.trim().length == 0) {
        alert("ID가 입력되지 않았습니다.");
        document.getElementById("userId").focus();
        return false;
    }
    if (document.getElementById("userId").value.trim().length < 2 || document.getElementById("userId").value.trim().length > 20) {
        alert("ID는 2글자 이상, 20글자 이하로 입력해주세요.");
        document.getElementById("userId").focus();
        return false;
    }

    if (document.getElementById("name").value.trim().length == 0) {
        alert("이름이 입력되지 않았습니다.");
        document.getElementById("name").focus();
        return false;
    }
    if (document.getElementById("name").value.trim().length < 2 || document.getElementById("name").value.trim().length > 10) {
         alert("이름은 2글자 이상, 10글자 이하로 입력해주세요.");
         document.getElementById("name").focus();
         return false;
    }

    if (document.getElementById("password").value.trim().length == 0) {
        alert("비밀번호가 입력되지 않았습니다.");
        document.getElementById("password").focus();
        return false;
    }

    if (document.getElementById("money").value.trim().length == 0) {
        alert("잔액이 입력되지 않았습니다.");
        document.getElementById("money").focus();
        return false;
    }
    if (isNaN(document.getElementById("money").value.trim())) {
        alert("잔액은 숫자로 입력해주세요.");
        document.getElementById("money").focus();
        return false;
    }

    alert("수정이 완료되었습니다.");
    document.getElementById("form").submit();
    return true;
}