(ns ctray.cmd
  (:require [ctray.state :refer [*pref-state *runtime-state init-pref-state!]]
           [clojure.java.io :as io]
           [clojure.core.async]
           [seesaw.core]
           )
 (:import [javax.swing Box]
          [javax.swing BorderFactory]
           )
    (:gen-class))



;; A common task it to load a file into a byte array.
(defn file->bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

;(clojure.java.shell/sh "cmd" "/c" "dir" :out-enc  "sjis") 
(defn scpanel
   ( []
     ( let [ ta     (seesaw.core/text :id :commandinput  :multi-line? true    )
             tf     (seesaw.core/text :id :commandoutput :multi-line? false   )
             sp     (seesaw.core/scrollable ta :id :scroll )
             enter  (seesaw.core/button :id :enter :text "Enter")
             clear  (seesaw.core/button :id :clear :text "Clear")
             bx     (doto (Box/createHorizontalBox)
                          (.setBorder (BorderFactory/createEmptyBorder 5 5 5 5 ))
                          (.add (Box/createHorizontalGlue))
                          (.add tf)
                          (.add (Box/createHorizontalStrut 5))
                          (.add enter)
                          (.add (Box/createHorizontalStrut 5))
                          (.add clear)
                         )
             bp  (seesaw.core/border-panel :id :command-panel
                 :center sp
                 :vgap 5 :hgap 5 :border 5
                 :south bx
                 )
            enter-action (seesaw.core/action :name "Enter" :handler (fn[e](prn e) ))
            clear-action (seesaw.core/action :name "Clear" :handler (fn[e]( seesaw.core/config! tf :text "")  ))
            ]
            (do 
                (seesaw.core/config! enter :action enter-action)
                (seesaw.core/config! clear :action clear-action)
                bp
            )
      )
    )
)

(defn showscp   ([] (showscp (scpanel) "SC"  ))
                ([bp] (showscp "SC"  ))
                ([bp title]
                 ( let [ cf     (seesaw.core/frame :title title)
                         scpanel(scpanel)
                        ]
                    (do
                     (seesaw.core/config! cf :content bp) 
                     (.setSize cf 300 300)
                     cf
                     )
                 )
                )
)


(defn cmd-frame-map
   ( [] (cmd-frame-map "cmd-frame"))
   ( [title] 
     ( let [ cframe (seesaw.core/frame :title title)
             cpanel (scpanel)
             ta     (seesaw.core/select cpanel [:#commandinput] )
             tf     (seesaw.core/select cpanel [:#commandoutput] )
             enter  (seesaw.core/select cpanel [:#enter] )
             clear  (seesaw.core/select cpanel [:#clear] )
             
             pb (doto (ProcessBuilder.  [] )  (.redirectErrorStream  false) (.command  ["cmd"]))
             pi (.start pb)
             rdr (doto (clojure.java.io/reader (.getInputStream  pi ) :encoding "SJIS" )
                       (prn)
                       (#(clojure.core.async/go-loop [rdr %] (do (.append ta (.readLine rdr))(.append ta "\r\n") (.setCaretPosition ta (.getLength (.getDocument ta) )) (if true  (do (recur rdr))) ) ))
                 )
             wrr (clojure.java.io/writer (.getOutputStream pi ) :encoding "SJIS")
             enter-action (seesaw.core/action :name "Enter" :handler (fn[e](do 
                                                                                 (.write wrr (seesaw.core/config tf :text ) )   
                                                                                 (.write wrr "\r\n" )
                                                                                 (.flush wrr)
                                                                                 (seesaw.core/config! tf :text "") ) ))
             clear-action (seesaw.core/action :name "Clear" :handler (fn[e] (do 
                                                                             (seesaw.core/config! ta :text "")  
                                                                             (seesaw.core/config! tf :text "") 
                                                                            )
                                                                     )
                           )
             
            ]
            (do 
                (seesaw.core/config! ta :editable? false)
                (seesaw.core/config! tf :action enter-action)
                (seesaw.core/config! enter :action enter-action)
                (seesaw.core/config! clear :action clear-action)
                (seesaw.core/config! cframe :content cpanel) 
                (.setSize cframe 300 300)
                cframe
                )
            )
     )
)

(defn- initcf
   ([] (initcf "SC"  ))
   ( [title]
     ( let [ cf     (seesaw.core/frame :title title)
             ta     (seesaw.core/text :id :commandinput  :multi-line? true    )
             tf     (seesaw.core/text :id :commandoutput :multi-line? false   )
             enter  (seesaw.core/button :id :enter :text "Enter")
             clear  (seesaw.core/button :id :clear :text "Clear")
             bx     (doto (Box/createHorizontalBox)
                          (.setBorder (BorderFactory/createEmptyBorder 5 5 5 5 ))
                          (.add (Box/createHorizontalGlue))
                          (.add tf)
                          (.add (Box/createHorizontalStrut 5))
                          (.add enter)
                          (.add (Box/createHorizontalStrut 5))
                          (.add clear)
                         )
             bp  (seesaw.core/border-panel 
                 ;:north (horizontal-panel :items rbs)
                 :center ta
                 :vgap 5 :hgap 5 :border 5
                 :south bx
                 )
            enter-action (seesaw.core/action :name "Enter" :handler (fn[e](prn e) ))
            clear-action (seesaw.core/action :name "Clear" :handler (fn[e](seesaw.core/config! (seesaw.core/select bp [:#commandinput] ) :text "")  ))
            ]
            (do 
                (seesaw.core/config! enter :action enter-action)
                (seesaw.core/config! clear :action clear-action)
                (seesaw.core/config! cf :content bp) 
                (.setSize cf 300 300)
                cf
                )
            )
     )
)


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
