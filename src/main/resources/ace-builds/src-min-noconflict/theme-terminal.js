ace.define("ace/theme/terminal",["require","exports","module","ace/lib/dom"],function(e,t,n){t.isDark=!0,t.cssClass="ace-builds-terminal-theme",t.cssText=".ace-builds-terminal-theme .ace_gutter {background: #1a0005;color: steelblue}.ace-builds-terminal-theme .ace_print-margin {width: 1px;background: #1a1a1a}.ace-builds-terminal-theme {background-color: black;color: #DEDEDE}.ace-builds-terminal-theme .ace_cursor {color: #9F9F9F}.ace-builds-terminal-theme .ace_marker-layer .ace_selection {background: #424242}.ace-builds-terminal-theme.ace_multiselect .ace_selection.ace_start {box-shadow: 0 0 3px 0px black;}.ace-builds-terminal-theme .ace_marker-layer .ace_step {background: rgb(0, 0, 0)}.ace-builds-terminal-theme .ace_marker-layer .ace_bracket {background: #090;}.ace-builds-terminal-theme .ace_marker-layer .ace_bracket-start {background: #090;}.ace-builds-terminal-theme .ace_marker-layer .ace_bracket-unmatched {margin: -1px 0 0 -1px;border: 1px solid #900}.ace-builds-terminal-theme .ace_marker-layer .ace_active-line {background: #2A2A2A}.ace-builds-terminal-theme .ace_gutter-active-line {background-color: #2A112A}.ace-builds-terminal-theme .ace_marker-layer .ace_selected-word {border: 1px solid #424242}.ace-builds-terminal-theme .ace_invisible {color: #343434}.ace-builds-terminal-theme .ace_keyword,.ace-builds-terminal-theme .ace_meta,.ace-builds-terminal-theme .ace_storage,.ace-builds-terminal-theme .ace_storage.ace_type,.ace-builds-terminal-theme .ace_support.ace_type {color: tomato}.ace-builds-terminal-theme .ace_keyword.ace_operator {color: deeppink}.ace-builds-terminal-theme .ace_constant.ace_character,.ace-builds-terminal-theme .ace_constant.ace_language,.ace-builds-terminal-theme .ace_constant.ace_numeric,.ace-builds-terminal-theme .ace_keyword.ace_other.ace_unit,.ace-builds-terminal-theme .ace_support.ace_constant,.ace-builds-terminal-theme .ace_variable.ace_parameter {color: #E78C45}.ace-builds-terminal-theme .ace_constant.ace_other {color: gold}.ace-builds-terminal-theme .ace_invalid {color: yellow;background-color: red}.ace-builds-terminal-theme .ace_invalid.ace_deprecated {color: #CED2CF;background-color: #B798BF}.ace-builds-terminal-theme .ace_fold {background-color: #7AA6DA;border-color: #DEDEDE}.ace-builds-terminal-theme .ace_entity.ace_name.ace_function,.ace-builds-terminal-theme .ace_support.ace_function,.ace-builds-terminal-theme .ace_variable {color: #7AA6DA}.ace-builds-terminal-theme .ace_support.ace_class,.ace-builds-terminal-theme .ace_support.ace_type {color: #E7C547}.ace-builds-terminal-theme .ace_heading,.ace-builds-terminal-theme .ace_string {color: #B9CA4A}.ace-builds-terminal-theme .ace_entity.ace_name.ace_tag,.ace-builds-terminal-theme .ace_entity.ace_other.ace_attribute-name,.ace-builds-terminal-theme .ace_meta.ace_tag,.ace-builds-terminal-theme .ace_string.ace_regexp,.ace-builds-terminal-theme .ace_variable {color: #D54E53}.ace-builds-terminal-theme .ace_comment {color: orangered}.ace-builds-terminal-theme .ace_indent-guide {background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWNgYGBgYLBWV/8PAAK4AYnhiq+xAAAAAElFTkSuQmCC) right repeat-y;}";var r=e("../lib/dom");r.importCssString(t.cssText,t.cssClass)});                (function() {
                    ace.require(["ace/theme/terminal"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            