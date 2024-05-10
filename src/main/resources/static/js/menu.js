// 선택한 메뉴를 저장할 배열
var selectedMenus = [];

// 메뉴를 선택할 때마다 호출되는 함수
function addToOrder(button) {
    var menuName = button.querySelector('.menuName').value;
    var menuPrice = button.querySelector('.menuPrice').value;

    // 배열에서 메뉴 찾기
    var existingMenu = selectedMenus.find(menu => menu.menuName === menuName);

    if (existingMenu) {
        // 메뉴가 이미 선택된 경우 수량만 증가
        existingMenu.quantity += 1;
    } else {
        // 새 메뉴 추가
        selectedMenus.push({ menuName: menuName, menuPrice: menuPrice, quantity: 1 });
    }

    // 선택한 메뉴를 화면에 표시
    updateSelectedMenu();
}



// 선택한 메뉴를 화면에 표시하는 함수
function updateSelectedMenu() {
    var selectedMenuDiv = document.getElementById('menu-order-view');
    selectedMenuDiv.innerHTML = ''; // 기존의 내용을 지우고 다시 그림

    var totalSum = 0; // 총 금액을 저장할 변수
    var formatter = new Intl.NumberFormat('ko-KR', { // 한국 통화 형식
            style: 'decimal',
            maximumFractionDigits: 0
        });

    selectedMenus.forEach(function(menu, index) {
        var menuDiv = document.createElement('div');
        menuDiv.className = 'order-item';
        var totalPrice = menu.menuPrice * menu.quantity; // 총 가격 계산
        totalSum += totalPrice; // 총 금액에 추가
        var formattedPrice = formatter.format(totalPrice); // 포맷된 가격

        menuDiv.innerHTML = `
            <div class="ordered-menu" style="display: flex; width: 100%; align-items: center;">
                <span><img src="/img/${menu.menuName}.jpg" alt="${menu.menuName}" style="width:100px; height:20%;">${menu.menuName}</span>
                <span style="padding-right : 5px; "><button onclick="removeMenuItem(${index})" class="menu-cancel-btn">⨉</button></span>
            </div>
            <div class="quantity-controls">
            <span>
                <button onclick="changeQuantity(${index}, -1)">-</button>
                <span>${menu.quantity}</span>
                <button onclick="changeQuantity(${index}, 1)">+</button>
            </span>
            <span>${formattedPrice}원</span>
            </div>
        `;
        selectedMenuDiv.appendChild(menuDiv);
    });
    // 총 금액을 order-price-total 요소에 업데이트
        document.getElementById('total-price').innerText = formatter.format(totalSum);
}


function changeQuantity(index, delta) {
    var menu = selectedMenus[index];
    var newQuantity = menu.quantity + delta;
    if (newQuantity < 1) {
        alert('최소 주문 수량은 1개입니다.');
        newQuantity = 1;
    }
    menu.quantity = newQuantity;
    updateSelectedMenu(); // UI 갱신
}

// 메뉴 수량 업데이트 함수
function updateQuantity(input, index) {
    var quantity = parseInt(input.value);
    selectedMenus[index].quantity = quantity;  // 배열 내 메뉴 수량 업데이트
}

// 메뉴 삭제 함수
function removeMenuItem(index) {
    selectedMenus.splice(index, 1);  // 배열에서 메뉴 제거
    updateSelectedMenu();  // 화면 업데이트
}


// 구매하기 버튼을 눌렀을 때 호출되는 함수
function submitOrder() {
    // 선택한 메뉴들을 서버에 전송
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/user/userMenu', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log('주문이 성공적으로 저장되었습니다.');
                alert('구매가 완료되었습니다.'); // 성공 메시지 표시
            } else {
                console.error('주문 처리 중 오류가 발생했습니다.');
                alert(JSON.parse(xhr.responseText).error); // 서버에서 보낸 오류 메시지 표시
            }
        }
    };
    xhr.send(JSON.stringify(selectedMenus));

    // 선택한 메뉴 배열 초기화
    selectedMenus = [];

    // 선택한 메뉴 화면 초기화
    updateSelectedMenu();
}



