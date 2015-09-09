"use strict";
// Client side paging
var Imtech = {};
Imtech.Pager = function () {
    this.paragraphsPerPage = 5;
    this.currentPage = 1;
    this.pagingControlsContainer = '#pagingControls';
    this.pagingContainerPath = '#content';
    this.elemTagName = 'div';

    this.numPages = function () {
        var numPages = 0;
        if (this.paragraphs != null && this.paragraphsPerPage != null) {
            numPages = Math.ceil(this.paragraphs.length / this.paragraphsPerPage);
        }
        return numPages;
    };

    this.showPage = function (page) {
        this.currentPage = page;
        var html = '';
        var self = this;

        this.paragraphs.slice((page - 1) * this.paragraphsPerPage,
            ((page - 1) * this.paragraphsPerPage) + this.paragraphsPerPage).each(function () {
                html += "<" + self.elemTagName + ">" + $(this).html() + "</" + self.elemTagName + ">";
            });
        $(this.pagingContainerPath).html(html);

        renderControls(this.pagingControlsContainer, this.currentPage, this.numPages());
    }

    var renderControls = function (container, currentPage, numPages) {
        var pagingControls = '<ul>';

        if (numPages <= 1) {
            return;
        }

        for (var i = 1; i <= numPages; i++) {
            if (i != currentPage) {
                pagingControls += '<li><a href="#" onclick="pager.showPage(' + i + '); return false;">' + i + '</a></li>';
            } else {
                pagingControls += '<li><span>' + i + '</span></li>';
            }
        }

        pagingControls += '</ul>';

        // todo: extract reference to pager from onclick: better register callback

        $(container).html(pagingControls);
    }
}
