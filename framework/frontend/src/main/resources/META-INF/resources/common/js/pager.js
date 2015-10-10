"use strict";
// Client side paging
(function (pager, $) {
  var currentPage = 1;

  pager.paragraphsPerPage = 5;
  pager.pagingControlsContainer = '#pagingControls';
  pager.pagingContainerPath = '#content';
  pager.elemTagName = 'div';
  pager.paragraphs = [];

  function numPages() {
    var numPages = 0;
    if (pager.paragraphs != null && pager.paragraphsPerPage != null) {
      numPages = Math.ceil(pager.paragraphs.length / pager.paragraphsPerPage);
    }
    return numPages;
  }

  function renderControls(container, currentPage, numPages) {
    var pagingControls = '<ul>';

    if (numPages <= 1) {
      return;
    }

    for (var i = 1; i <= numPages; i++) {
      // todo extract onclick
      if (currentPage == i) {
        pagingControls += '<li><span>' + i + '</span></li>';
      } else {
        pagingControls += '<li><a href="#" class="pagerLink" data-row-id="' + i + '">' + i + '</a></li>';
      }
    }

    pagingControls += '</ul>';
    //onclick="pager.showPage(' + i + '); return false;"

    $(container).html(pagingControls);
    $("li>a.pagerLink").on("click", function (evt) {
      pager.showPage(evt.target.attributes['data-row-id'].value);
      evt.preventDefault();
    });
  }

  pager.showPage = function (page) {
    var html = '',
      self = this;

    currentPage = page;

    pager.paragraphs.slice((page - 1) * pager.paragraphsPerPage,
      ((page - 1) * pager.paragraphsPerPage) + pager.paragraphsPerPage).each(function () {
        html += "<" + self.elemTagName + ">" + $(this).html() + "</" + self.elemTagName + ">";
      });
    $(pager.pagingContainerPath).html(html);

    renderControls(pager.pagingControlsContainer, currentPage, numPages());
  }

})(window.pager = window.pager || {}, jQuery);





