function addToOrder(menuName, menuPrice) {
    var menuOrderView = document.querySelector('.menu-order-view');

    // 메뉴 정보를 표시할 새로운 요소를 생성합니다.
    var menuItem = document.createElement('div');
    menuItem.textContent = menuName + ' - ' + menuPrice + '원';

    // 생성한 요소를 주문 내역 창에 추가합니다.
    menuOrderView.appendChild(menuItem);
}
