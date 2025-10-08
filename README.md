2025.10.02 기준 비트연산자를 이용하여 검색 로직을 구성하고 검색 결과 불러오기
* 비트 연산자 활용이 목적

1. App 실행시 Read Word File 버튼을 선택하여 내장File 불러오기
2. 검색단어 입력
3. Search 버튼 클릭하여 검색 결과 출력
   a. file에 없는 단어 : '존재 하지 않는 단어입니다.' 안내 문구 출력
   b. 알파벳 중복 : '알파벳 중복 없는 단어로 다시 검색해주세요' 안내 문구 출력
   c. 단어 검색 성공 : 검색단어, indexNo 출력
4. 알파벳 중복 uniqueCheck() 로직 수정이 필요
5. 현재 중복 확인 가능한 범위 : 알파벳 소문자, 대문자 -> 추후 숫자, 한글 등 추가 필요
6. 추가 필요한 기능 : Add 버튼 클릭시 단어 추가, searchView 활용한 검색 기능, Multithreading 활용 

<img width="2800" height="1752" alt="Screenshot_20251008_223645" src="https://github.com/user-attachments/assets/4dabbcce-38d6-4b28-ac01-09d57866f41a" />

<img width="2800" height="1752" alt="image" src="https://github.com/user-attachments/assets/952c816a-f509-4533-b321-0ed0ab51ebb3" />

<img width="2800" height="1752" alt="image" src="https://github.com/user-attachments/assets/39b7fec5-e12d-4371-ba9e-d72e31a0fba5" />

<img width="2800" height="1752" alt="image" src="https://github.com/user-attachments/assets/5401c545-3639-4aea-acc2-17695897cbb7" />

<img width="2800" height="1752" alt="image" src="https://github.com/user-attachments/assets/baa31e3e-b660-4c92-bd51-e4b0fb1bcbbd" />






