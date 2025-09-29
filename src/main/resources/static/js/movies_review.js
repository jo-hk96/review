//별점 클릭 이벤트 핸들러
document.querySelectorAll('.rating-area .star').forEach(star => {
    // 클릭 이벤트: 숨겨진 input의 value를 업데이트하고 'on' 클래스 적용
    star.addEventListener('click', function() {
        const rating = this.getAttribute('data-rating');
        
        // 숨겨진 input 필드에 값 저장
        document.getElementById('selected-rating').value = rating; 
        
        // 'on' 클래스 초기화 및 재적용
        const allStars = this.parentElement.querySelectorAll('.star');
        allStars.forEach(s => s.classList.remove('on'));
        
        let currentStar = this;
        while (currentStar) {
            currentStar.classList.add('on');
            currentStar = currentStar.previousElementSibling; 
        }
    });

    // 마우스 leave 이벤트: 선택된 별점까지 색상을 유지하도록 처리
    star.parentElement.addEventListener('mouseleave', function() {
        const selectedRating = parseInt(document.getElementById('selected-rating').value);
        
        document.querySelectorAll('.rating-area .star').forEach(s => {
            const starRating = parseInt(s.getAttribute('data-rating'));
            if (starRating <= selectedRating) {
                s.classList.add('on');
            } else {
                s.classList.remove('on');
            }
        });
    });
    
    // 마우스 enter 이벤트: 호버 시 모든 별 색칠
    star.parentElement.addEventListener('mouseenter', function() {
        document.querySelectorAll('.rating-area .star').forEach(s => s.classList.remove('on'));
    });
});



document.getElementById('submit-review-btn').addEventListener('click', function() {
    const apiId = document.getElementById('apiId').value; // 영화 ID (TMDB ID)
    const nickname = document.getElementById('nickname').textContent;
    const comment = document.querySelector('textarea[name="comment"]').value;
    const rating = document.getElementById('selected-rating').value;

    if (comment.trim() === '' || rating === '0') {
        alert('리뷰 내용과 별점을 모두 입력해 주세요.');
        return;
    }
    
    const reviewData = {
        movieId: apiId,
        nickname: nickname,
        comment: comment,
        rating: parseInt(rating)
    };

    fetch('/api/userReview', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reviewData)
    })
    .then(response => {
        if (!response.ok) {
        	
        	//에러발생
            throw new Error('리뷰 전송 실패: ' + response.status);
        }
        return response.json(); // ⭐ 서버가 반환한 JSON(ReviewResponseDto)을 받음
    })
    .then(newReviewData => {
        // ⭐⭐ 1. 서버 응답을 받아 HTML 생성 및 추가 ⭐⭐
        const reviewHtml = createReviewHtml(newReviewData);
        
        // 리뷰 목록 컨테이너 (예: <div id="review-list">)의 가장 위에 추가
        document.getElementById('review-list').insertAdjacentHTML('afterbegin', reviewHtml); 

        // 2. 입력 폼 초기화 (리뷰 내용, 별점 초기화)
        document.querySelector('textarea[name="comment"]').value = '';
        document.getElementById('selected-rating').value = '0';
        document.querySelectorAll('.rating-area .star').forEach(star => star.style.color = 'gray');
        
        alert('리뷰가 등록되었습니다.');
    })
    .catch(error => {
        console.error('리뷰 전송 중 오류 발생:', error);
        alert('리뷰 등록 중 오류가 발생했습니다. 로그를 확인하세요.');
    });
});


function generateStars(rating) {
    const count = Math.round(rating); 
    const filledStars = '★'.repeat(count); // 별점 수만큼 ★ 반복
    
    // 스타일을 gold로 지정하여 별점이 눈에 띄게
    return `<span style="color: gold;">${filledStars}</span>`; 
}

function createReviewHtml(review) {
    // 서버에서 받은 데이터를 사용하여 HTML을 동적으로 생성
    // ⭐ generateStars 함수를 호출하도록 수정 ⭐
    const starHtml = generateStars(review.rating);
    
    return `
        <div style="border: 1px solid #ccc; margin-bottom: 10px; padding: 10px;">
            <p>
                <strong>작성자:</strong> 
                <span>${review.nickname}</span>
            </p>
            <table>
                <tr>
                    <td id="reviewsRating">
                        <strong>평점:</strong> 
                        <span>${starHtml}</span> 
                    </td>
                </tr>
                <tr>
                    <td>
                        <p style="margin: 0;">${review.comment}</p>
                    </td>
                </tr>
            </table>
            
            <p id="reviewsRegdate">
                작성일: <span>${review.regDate}</span>
            </p>
        </div>
    `;
}
