 function addToOrder(menuName, menuPrice) {
        var orderView = document.querySelector('.menu-order-view');
        orderView.innerHTML += "<div>" + menuName + " - " + menuPrice + "원</div>";
 } //주문내역 보여주는 js