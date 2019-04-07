ace.define("ace/theme/eclipse",["require","exports","module","ace/lib/dom"],function(e,t,n){"use strict";t.isDark=!1,t.cssText='.ace-builds-eclipse .ace_gutter {background: #ebebeb;border-right: 1px solid rgb(159, 159, 159);color: rgb(136, 136, 136);}.ace-builds-eclipse .ace_print-margin {width: 1px;background: #ebebeb;}.ace-builds-eclipse {background-color: #FFFFFF;color: black;}.ace-builds-eclipse .ace_fold {background-color: rgb(60, 76, 114);}.ace-builds-eclipse .ace_cursor {color: black;}.ace-builds-eclipse .ace_storage,.ace-builds-eclipse .ace_keyword,.ace-builds-eclipse .ace_variable {color: rgb(127, 0, 85);}.ace-builds-eclipse .ace_constant.ace_buildin {color: rgb(88, 72, 246);}.ace-builds-eclipse .ace_constant.ace_library {color: rgb(6, 150, 14);}.ace-builds-eclipse .ace_function {color: rgb(60, 76, 114);}.ace-builds-eclipse .ace_string {color: rgb(42, 0, 255);}.ace-builds-eclipse .ace_comment {color: rgb(113, 150, 130);}.ace-builds-eclipse .ace_comment.ace_doc {color: rgb(63, 95, 191);}.ace-builds-eclipse .ace_comment.ace_doc.ace_tag {color: rgb(127, 159, 191);}.ace-builds-eclipse .ace_constant.ace_numeric {color: darkblue;}.ace-builds-eclipse .ace_tag {color: rgb(25, 118, 116);}.ace-builds-eclipse .ace_type {color: rgb(127, 0, 127);}.ace-builds-eclipse .ace_xml-pe {color: rgb(104, 104, 91);}.ace-builds-eclipse .ace_marker-layer .ace_selection {background: rgb(181, 213, 255);}.ace-builds-eclipse .ace_marker-layer .ace_bracket {margin: -1px 0 0 -1px;border: 1px solid rgb(192, 192, 192);}.ace-builds-eclipse .ace_meta.ace_tag {color:rgb(25, 118, 116);}.ace-builds-eclipse .ace_invisible {color: #ddd;}.ace-builds-eclipse .ace_entity.ace_other.ace_attribute-name {color:rgb(127, 0, 127);}.ace-builds-eclipse .ace_marker-layer .ace_step {background: rgb(255, 255, 0);}.ace-builds-eclipse .ace_active-line {background: rgb(232, 242, 254);}.ace-builds-eclipse .ace_gutter-active-line {background-color : #DADADA;}.ace-builds-eclipse .ace_marker-layer .ace_selected-word {border: 1px solid rgb(181, 213, 255);}.ace-builds-eclipse .ace_indent-guide {background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==") right repeat-y;}',t.cssClass="ace-builds-eclipse";var r=e("../lib/dom");r.importCssString(t.cssText,t.cssClass)});                (function() {
                    ace.require(["ace/theme/eclipse"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            