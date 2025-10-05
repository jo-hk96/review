document.addEventListener('DOMContentLoaded', function() {
	    const scrollBtn = document.getElementById("scrollToTopBtn");
	
	    // 1. 스크롤 시 버튼 표시/숨김 처리
	    window.onscroll = function() {
	        // 스크롤 위치가 20px를 넘어가면 버튼을 표시합니다.
	        if (document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) {
	            scrollBtn.style.display = "block";
	        } else {
	            scrollBtn.style.display = "none";
	        }
	    };
	
	    // 2. 클릭 시 맨 위로 이동
	    scrollBtn.addEventListener('click', function() {
	        // 부드러운 스크롤 효과와 함께 맨 위로 이동 (최신 브라우저 지원)
	        window.scrollTo({
	            top: 0,
	            behavior: 'smooth' 
	        });
	    });
	});