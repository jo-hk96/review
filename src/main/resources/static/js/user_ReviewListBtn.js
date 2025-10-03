

	//평점 숫자 마다 별 갯수 변화
	function generateStars(rating) {
    const fullStars = Math.floor(rating); 
    
    return '⭐'.repeat(fullStars);
}

	//좋아요 목록을 가져오는 AJAX 함수 (TMDB API 호출)
	function loadUserReviews(){
		const ReviewMoviesContainer = document.getElementById('ReviewMoviesContainer');
		const emptyMessage = document.getElementById('emptyMessage');
	    
	    if (!ReviewMoviesContainer || !emptyMessage) {
	        console.error("리뷰 모달의 컨테이너 요소를 찾을 수 없습니다.");
	        return;
	    }
	
	// 로딩 메시지 표시
	ReviewMoviesContainer.innerHTML = '<p>사용자의 리뷰 목록을 가져오고있습니다...</p>';
	emptyMessage.style.display = 'none'; // 메시지 숨김
	
	// 좋아요 목록을 JSON으로 반환하는 Controller API 엔드포인트로 요청
	fetch('/api/user/ReviewMovie') //Controller에서 JSON 반환 필요
		.then(response =>{
			if(!response.ok){
				throw new Error('리뷰 목록 가져오기 실패' + response.status + ')');
			}
			return response.json();			
		})
		.then(data => {
			renderReviewMovies(data); // 성공적으로 데이터를 받으면 렌더링
		})	
		.catch(error =>{
			console.error('영화 리뷰 목록 에러:', error);
			ReviewMoviesContainer.innerHTML = '<p style = "color:red;">데이터 로딩 오류. 다시시도해주세요.</p>';	
		});
}

	//목록을 모달에 렌더링하는 함수
	function renderReviewMovies(movies) {
	    const ReviewMoviesContainer = document.getElementById('ReviewMoviesContainer');
	    const emptyReviewMessage = document.getElementById('emptyReviewMessage');
	    
	    if (!ReviewMoviesContainer || !emptyReviewMessage) return;
	
	    if(movies && movies.length > 0){
	        let html = '<ul class="review-list">';
	        movies.forEach(review =>{
			const stars = generateStars(review.rating);
	           html += `
                <li class="review-item">
                    <div class="review-header">
                    	<hr>
                        <strong class="movie-title">${review.title}</strong> 
                        <span class="review-rating">${stars}</span>
                    </div>
                    <p class="review-comment">${review.comment}</p>
                    <small class="review-date">작성일: ${review.regDate ? new Date(review.regDate).toLocaleDateString() : '날짜정보 없음'}</small>
                </li>
           		`;
        	});
        html += '</ul>';
	        ReviewMoviesContainer.innerHTML = html;
	        emptyReviewMessage.style.display = 'none';
	        }else{
	            // 리뷰 목록이 비어있을 경우
	            ReviewMoviesContainer.innerHTML ='';
	            emptyReviewMessage.style.display = 'block';
	        }
	}
	
	//버튼 클릭 핸들러 (openLikeModal)
	function openReviewModal(){
	    const ReviewModal = document.getElementById('ReviewModal');
	    
	    if(ReviewModal){
	        ReviewModal.style.display = 'block';
	        
	        document.body.classList.add('modal-open');
	        
	        loadUserReviews();
	    }else{
	        console.error("모달을 찾을수없습니다.");
	    }
	}

	//이벤트 리스너 등록 (DOMContentLoaded)
	document.addEventListener('DOMContentLoaded', function() {
	    const ReviewModal = document.getElementById('ReviewModal');
	    const closeBtn2 = document.querySelector('.close-btn2');
	    if (!ReviewModal || !closeBtn2) return; 
	    
	    closeBtn2.onclick = function() {
	        ReviewModal.style.display = 'none';
	        document.body.classList.remove('modal-open');
	    };
	    
	    window.onclick = function(event) {
	        if (event.target === ReviewModal) {
	            ReviewModal.style.display = 'none';
	             document.body.classList.remove('modal-open');
	        }
	    };
	});