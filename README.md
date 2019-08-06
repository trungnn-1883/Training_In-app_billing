# Training_In-app_billing

## I. Tổng quan

### 1. Khái niệm chung

 Một số hình thức kiếm tiền từ app:

- Paid: trả tiền trước rồi mới được sở hữu ứng dụng

- Free-to-use: down về miễn phí, người dùng có thể sử dụng được hết các tính năng trong app. Nhưng giả sử  trong game muốn có đồ tốt hơn, skill mạnh, trang phục, ... sẽ phải mua 

- Freemium: down về miễn phí, nhưng muốn mua thêm các tính năng mới, tiện ích mới thì phải mua.

- Ad-funded: kiếm tiền qua quảng cáo

 Những cách hay dùng bây giờ là Free-to-use vs Freemium. Chúng sử dụng in-app billing, là tính năng được tích hợp bên trong ứng dụng để giúp người dùng có thể mua những bổ sung, những tiện ích thêm trong ứng dụng

 Trong Android, ta sử dụng Google Play Billing - dịch vụ giúp bán những nội dung số.

 Một số ví dụ như trong game ta có thể mua thêm lượt chơi, mua thêm vật phẩm, thêm màn chơi, thêm tiện ích, ... Trong app khác thì ta có thể mua thêm các gói chức năng, tiện ích, công cụ nâng cao. Sử dụng các app như Netflix, Spotify, ... phải trả phí sử dụng hàng tháng. 

### 2. Các loại sản phẩm

Có 3 loại

- One-time: mua 1 hoặc nhiều lần, không định kì. Ví dụ như màn chơi, bài hát, film, ...

<img src="https://i.redd.it/ax07ywpytnd21.jpg" width="200">

- Rewarded: yêu cầu người dùng xem quảng cáo video. Sau khi xem người chơi sẽ có thêm mạng, hoàn thành nhanh, vật phẩm mới, ...

<img src="https://newapplift-production.s3.amazonaws.com/comfy/cms/files/files/000/003/415/original/RewardedVideoAd.png" width="200">

- Subscription: yêu cầu người dùng thanh toán định kì, theo tháng, theo quý, năm, ...

<img src="https://sweetpricing.com/blog/wp-content/uploads/2016/09/in-app-subscriptions-3-787x394.png"/>

### 3. Purchase token và order Ids

Google Play Billing quản lý sản phẩm và phiên làm việc thông qua purchase token và order id:

- Purchase token: chuỗi token đại diện cho quyền lợi của người dùng tới 1 sản phẩm trên Google Play. Nó chỉ thị người dùng đã trả tiền cho 1 sản phẩm cụ thể, đại diện bởi 1 SKU 

- Order id: chuỗi đại diện cho 1 phiên giao dịch tài chính trên Google Play. Chuỗi này được bao gồm trong hóa đơn mà được gửi mail tới người mua. Còn được dùng trong báo cáo bán hàng và thu chi.

Vs sản phẩm one-time và rewarded, mỗi lần mua mới sẽ tạo ra 1 purchase token và order id mới

Vs sản phẩm subsription, mua lần đầu sẽ tạo mới purchase token và order id. Những lần sau thì purchase token giữ nguyên, order id sẽ được tạo mới. 

Nâng cấp, hạ cấp hay đăng kí lại sẽ đều tạo mới purchase token và order id.

### 4. Tùy chọn cho sản phẩm 

Có thể chỉnh nhiều thông số như:

- Tiêu đề

- Miêu tả 

- Product Id

- Giá cả 

Promo code: mã giảm giá giúp người dùng có sản phẩm one-time miễn phí. 

### 5. Tùy chọn sản phẩm subscription

Có thể chỉnh các thông số

- Thời hạn thanh toán

- Thời hạn thử miễn phí

- Giá giới thiệu 


