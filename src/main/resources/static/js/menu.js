// function addToOrder(clickedButton) {
//    var orderView = document.querySelector('.menu-order-view');
//    var menuName = clickedButton.querySelector(".menuName").value;
//    var menuPrice = clickedButton.querySelector(".menuPrice").value;
//    orderView.innerHTML += "<div>" + menuName + " - " + menuPrice + "원</div>";
//}// 메뉴 클릭하면 주문내역에 표시해주는 코드


// 선택한 메뉴를 저장할 배열
var selectedMenus = [];

// 메뉴를 선택할 때마다 호출되는 함수
function addToOrder(button) {
    var menuName = button.querySelector('.menuName').value;
    var menuPrice = button.querySelector('.menuPrice').value;

    // 선택한 메뉴를 배열에 추가
    selectedMenus.push({ menuName: menuName, menuPrice: menuPrice });

    // 선택한 메뉴를 화면에 표시
    updateSelectedMenu();
}

// 선택한 메뉴를 화면에 표시하는 함수
function updateSelectedMenu() {
    var selectedMenuDiv = document.getElementById('menu-order-view');
    selectedMenuDiv.innerHTML = ''; // 기존의 내용을 지우고 다시 그림

    selectedMenus.forEach(function(menu) {
        var menuDiv = document.createElement('div');
        menuDiv.textContent = menu.menuName + ' - ' + menu.menuPrice + '원';
        selectedMenuDiv.appendChild(menuDiv);
    });
}

// 구매하기 버튼을 눌렀을 때 호출되는 함수
function submitOrder() {
    // 선택한 메뉴들을 서버에 전송
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/user/userMenu', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            // 요청이 성공적으로 처리됐을 때 수행할 작업
            console.log('주문이 성공적으로 저장되었습니다.');
        }
    };

    xhr.send(JSON.stringify(selectedMenus));

    // 선택한 메뉴 배열 초기화
    selectedMenus = [];

    // 선택한 메뉴 화면 초기화
    updateSelectedMenu();
}
