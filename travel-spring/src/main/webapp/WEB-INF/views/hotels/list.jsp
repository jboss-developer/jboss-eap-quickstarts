<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h1>Hotel Results</h1>

<p>
    <a id="changeSearchLink"
       href="hotels/search?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}">Change
        Search</a>
    <script type="text/javascript">
        Spring.addDecoration(new Spring.AjaxEventDecoration({
            elementId: "changeSearchLink",
            event: "onclick",
            popup: true,
            params: {fragments: "searchForm"}
        }));
    </script>
</p>
<div id="hotelResults">
    <c:if test="${not empty hotelList}">
        <table class="summary">
            <thead>
            <tr>
                <th>Name</th>
                <th>Address</th>
                <th>City, State</th>
                <th>Zip</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="hotel" items="${hotelList}">
                <tr>
                    <td>${hotel.name}</td>
                    <td>${hotel.address}</td>
                    <td>${hotel.city}, ${hotel.state}, ${hotel.country}</td>
                    <td>${hotel.zip}</td>
                    <td><a href="hotels/${hotel.id}">View Hotel</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty hotelList}">
                <tr>
                    <td colspan="5">No hotels found</td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <div class="buttonGroup">
            <div class="span-3">
                <c:if test="${searchCriteria.page > 0}">
                    <a id="prevResultsLink"
                       href="hotels?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page - 1}">Previous
                        Results</a>
                    <script type="text/javascript">
                        Spring.addDecoration(new Spring.AjaxEventDecoration({
                            elementId: "prevResultsLink",
                            event: "onclick",
                            params: {fragments: "body"}
                        }));
                    </script>
                </c:if>
            </div>
            <div class="span-3 append-12 last">
                <c:if test="${not empty hotelList && fn:length(hotelList) == searchCriteria.pageSize}">
                    <a id="moreResultsLink"
                       href="hotels?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page + 1}">More
                        Results</a>
                    <script type="text/javascript">
                        Spring.addDecoration(new Spring.AjaxEventDecoration({
                            elementId: "moreResultsLink",
                            event: "onclick",
                            params: {fragments: "body"}
                        }));
                    </script>
                </c:if>
            </div>
        </div>
    </c:if>
</div>	

