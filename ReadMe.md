# Backend Hệ thống quản lý cửa hàng bánh ngọt

| STT |        Tên         |    MSSV     | Nhiệm vụ                 |
|:----|:------------------:|:-----------:|--------------------------|
| 1   |    Trần Vệt Anh    | N20DCCN087  | Quản lý hạn sử dụng bánh |
| 2   |  Lương Thanh Quý   | N20DCCN087  | Gọi món                  |
| 2   | Nguyễn Quang nghĩa | N20DCCN087  | Quản lý tồn kho          |
Tự mình làm được
- Quản lý khu vực, bàn có mã qr
- CRUD user
- CRUD bill
## Phân tích đồ án
### Hướng phân tích theo hướng đối tượng

- Quản lý hạn sử dụng ở đây vì vấn đề nằm ở cửa hàng bánh ngọt nào cũng có một vấn đề chung là sản xuất bánh hằng ngày nên case của mình sẽ phân tích về giúp người quản lý quản lý đươợc hạn sử dụng bánh đưa ra các giải pháp kịp thời với nó ví dụ: Bánh còn hạn để đó, bánh gần hết hạn phải được thông báo đẻ dán nhãn lên đồng thời thì cũng trưng bày ra truước cho khách hàng thấy
  - Case quản lý trạng thái bánh có 4 trạng thái: Còn hạn, gần hết hạn, hết hạn, đã huỷ
    - Còn hạn khi mà bánh vừa được sản xuất ra sẽ được gán là còn hạn
    - Gần hết hạn mình quản lý thông qua một field là shelf_life_days_limit đây là thời gian giới hạn của sản phẩm nếu sản phẩm thuộc trong này thì xếp vào gần hết hạn
    - Còn hết hạn khỏi phải nói đơn giản hết hạn thôi
    - Huỷ khi mà quản lý yêu cầu bánh hết hạn huỷ  -> Huỷ
  - Làm sao khách hàng thấy được sản phẩm giảm giá? vậy mình đang bắt buộc khách mua hàng ưu đãi với ngày hết hạn gần nhất à?
    - Không mình sẽ hiển thị thông tin sản phẩm là sản phẩm có giảm giá sâu nhất: Khi nhấn vào chi tiết thì sẽ xuất hiện các option khác nhau để chọn có các lô hàng của nhiều ngày đồng thời có ngày đếm ngược.
    - Về phần quản lý thì quản lý sẽ có quản lý hạn sử dụng sẽ thống kê toàn bộ các lô sản phẩm đang được lưu hành trên cửa hàng đồng thời có 2 tuỳ chọn
      - Huỷ nhanh thống kê toàn bộ các lô hàng hiện đang là hết hạn để chuyển trạng thái sang huỷ và ghi lại quản lý huỷ
      - Giảm giá nhanh thống kê toàn bộ sản phẩm sapw shết hạn để người quản lý chọn áp dụng giảm giá ngày nhanh
        - Giảm giá theo % mong muốn
        - Giảm giá theo % cấu hình sẵn trong sản phẩm
        - Lấy sản ngày cuối cùng của một lô hàng có ngày cuối xa nhất
        - Lấy theo ngày mong muốn

## Các phase
### Phase 1 26-9-2024
    1. Tên đề tài
    2. Chương 1: Liên quan công nghệ (đề tài sử dụng công nghệ gì)
    3. Khảo sát hiện trạng của đề tài. (chương 2)

### Phase 2.1 10-10-2024
    Phân tích thiết kế hệ thống (chương 3)

### Phase 2.2 24-10-2024

    1. phân tích thiết kế phần còn lại (chương 3)
    2.Thiết kế tài liệu (thiết tài liệu khái quát, thiết kết tài liệu chi tiết)
    2.1Thiết kế tài liệu khái quát ( thiết kế tất cả các màn hình) -> vẽ bằng excel .... Tham khảo tài liệu thiết kế khái quát
    2.2 Thiết kế tài liệu chi tiết (chọn 3 cái màn hình)-> thiết kế tài liệu chi tiết cho 3 màn hình đã chọn ->Tham khảo tài liệu thiết kế chi tiết.
### Phase 2.3 31-10-2024
    Sửa các lỗi sai có trong các phase trước đó

### Phase 3 31-10-2024
    Hoàng thiện chương trình (chạy được) viết code
    -> Demo tại lớp chấm điểm


