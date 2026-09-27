# Giải thích: Vì sao chọn `@CacheEvict` thay vì `@CachePut` (Chiến lược Cache-Aside)

Trong bài toán cập nhật dữ liệu, ta dùng `@CacheEvict` (xóa cache) thay vì `@CachePut` (cập nhật đè lên cache) nhằm **tránh lỗi Race Condition** và **đảm bảo tính nhất quán (Consistency)**.

## Vấn đề nếu dùng `@CachePut`:
Nếu 2 request (A và B) cùng lúc thực hiện update trên cùng một bản ghi:
1. Request A update Database (DB).
2. Request B update Database (DB).
3. Request B dùng `@CachePut` ghi đè vào Cache.
4. Request A dùng `@CachePut` ghi đè vào Cache (do mạng chậm).

**Kết quả:** DB đang lưu dữ liệu của **B**, nhưng Cache lại lưu dữ liệu của **A**. Lỗi nhất quán dữ liệu (stale data) xảy ra.

## Giải pháp an toàn với `@CacheEvict` (Cache-Aside Pattern):
1. **Write:** Cập nhật DB trước. Sau đó dùng `@CacheEvict` để **xóa hoàn toàn** key cache đó.
2. **Read:** Lần người dùng đọc tiếp theo (Cache Miss), hệ thống sẽ chủ động query lại từ DB (đảm bảo lấy dữ liệu mới nhất), rồi tự động nạp lại vào Cache (thông qua `@Cacheable`).

*Việc xóa luôn an toàn và nhẹ hơn so với việc cố gắng đồng bộ lại (ghi đè) giá trị bộ nhớ đệm.*

---
## Hướng dẫn chụp ảnh Redis CLI (Để nộp bài):
Mở terminal và thực thi:

1. Gửi request GET để nạp cache:
`GET http://localhost:8080/menu/1`

2. Vào Redis CLI kiểm tra dữ liệu đã vào:
`redis-cli`
`GET "menuCache::1"`
*(Chụp màn hình thấy dữ liệu JSON của món ăn lúc chưa update)*

3. Gọi API Update món ăn lên 75000:
`PUT / POST http://localhost:8080/menu/1` với body JSON.

4. Kiểm tra lại Redis CLI:
`GET "menuCache::1"`
*(Kết quả ra `(nil)` - Chụp lại màn hình này chứng minh cache cũ đã bị xóa thành công)*
