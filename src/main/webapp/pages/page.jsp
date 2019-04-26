<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="box-footer">
    <div class="pull-left">
        <div class="form-group form-inline">
            总共${pageInfo.pages} 页，共${pageInfo.total} 条数据。

            每页
            <%--
            <select class="form-control">
                <option>10</option>
                <option>15</option>
                <option>20</option>
                <option>50</option>
                <option>80</option>
            </select>
            --%>
            <%--相当于：for(int i=1;i&lt=10; i++){}--%>
            <%--注意：默认选中每页显示的行--%>
            <select class="form-control" onchange="gotoPage(1,this.value)">
                <c:forEach var="num" begin="1" end="10" step="1">
                    <option value="${num}" ${num==pageInfo.pageSize?'selected':''}>${num}</option>
                </c:forEach>
            </select>

            条
        </div>
    </div>

    <div class="box-tools pull-right">
        <ul class="pagination">
            <li><a href="javascript:gotoPage(1,'${pageInfo.pageSize}')" aria-label="Previous">首页</a></li>
            <li><a href="javascript:gotoPage('${pageInfo.prePage}','${pageInfo.pageSize}')">上一页</a></li>

            <%-- 页码控制
            <li><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            --%>
            <c:forEach
                    var="p"
                    begin="${((pageInfo.pageNum-5)>0) ? (pageInfo.pageNum-5) : 1}"
                    end="${((pageInfo.pageNum+5)>pageInfo.pages) ? (pageInfo.pages) : (pageInfo.pageNum+5)}">
                <li>
                    <a href="javascript:gotoPage('${p}','${pageInfo.pageSize}')">
                        <c:choose>
                            <c:when test="${p==pageInfo.pageNum}">
                                <span style="color: red">${p}</span>
                            </c:when>
                            <c:otherwise>${p}</c:otherwise>
                        </c:choose>
                    </a>
                </li>
            </c:forEach>

            <li><a href="javascript:gotoPage('${pageInfo.nextPage}','${pageInfo.pageSize}')">下一页</a></li>
            <li><a href="javascript:gotoPage('${pageInfo.pages}','${pageInfo.pageSize}')" aria-label="Next">尾页</a></li>
        </ul>
    </div>
</div>

<script type="text/javascript">
    function gotoPage(pageNum,pageSize){
        // 分页提交数据到后台：1.地址、2.参数（当前页，页大小）

        // 设置分页参数
        $("#pageNum").val(pageNum);
        $("#pageSize").val(pageSize);
        // 提交表单
        document.forms[0].submit();
    }
</script>
