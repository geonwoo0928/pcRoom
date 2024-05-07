 function addToOrder(clickedButton) {
    var orderView = document.querySelector('.menu-order-view');
    var menuName = clickedButton.querySelector(".menuName").value;
    var menuPrice = clickedButton.querySelector(".menuPrice").value;
    orderView.innerHTML += "<div>" + menuName + " - " + menuPrice + "원</div>";
}// 메뉴 클릭하면 주문내역에 표시해주는 코드
