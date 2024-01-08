(ns com.j0suetm.components.about
  (:require
   [com.j0suetm.config :refer [config]]
   [helix.core :refer [$ defnc]]
   [helix.dom :as hd]
   [refx.alpha :as refx]))

(defnc social-links [{:keys [socials]}]
  (hd/div
   {:class "md:h-10 md:pl-2 md:flex md:flex-row md:items-center
            lg:pl-3"}
   (hd/div
    {:class "flex flex-row border-y border-black divide-x divide-black
             md:border-none md:divide-none"}
    (for [[link name] socials]
      (hd/a
       {:class "w-full py-2 text-center text-xl
                md:w-min md:px-2 md:py-0
                lg:px-3"
        :target "_blank"
        :href link}
       name)))
   (hd/div {:class "hidden w-full ml-2 border-t border-black
                    md:block
                    lg:ml-3"})
   (hd/button
    {:class "hidden w-fit link md:px-2 md-py-0 text-xl
             md:block"
     :on-click #(refx/dispatch-sync [:toggle-navbar true])}
    "Articles")))

(defnc header []
  (let [w-size (.. js/window -document -documentElement -clientWidth)
        sm? (< w-size 768)]
    (hd/header
     {:class "w-full px-4 py-4
              lg:text-end
              lg:py-8"}
     (hd/h1
      {:class "font-abrilff font-bold text-4xl
               lg:text-5xl
               xl:text-6xl"}
      (str "Josué" (if sm? " T. " " Teodoro ") "Moreira"))
     (hd/p
      {:class "font-playfair font-medium text-xl
               md:text-2xl
               xl:mt-2 xl:text-3xl"}
      "@j0suetm | Software Engineer"))))

(defnc interests-col [{:keys [interests]}]
  (hd/ul
   {:class "list-disc list-inside"}
   (for [interest interests]
     (hd/li interest))))

(defnc interests [{:keys [left right]}]
  (hd/div
   {:class "w-full my-8 pl-2 flex flex-col
            lg:flex-row lg:space-x-6
            xl:my-4"}
   ($ interests-col {:interests left})
   ($ interests-col {:interests right})))

(defnc latest-articles-cmp [{:keys [qtt]}]
  (let [articles (refx/use-sub [:raw-articles])]
    (hd/ul
     {:class "mt-8 pl-2 list-disc list-inside
             xl:mt-4"}
     (for [{:keys [title date]} (take qtt articles)]
       (hd/li
        (hd/a
         {:target "_blank"
          :href (str (:base-url config) "/articles/" title)}
         (let [human-date (-> (* date 1000)
                              js/Date.
                              (.toLocaleDateString "pt-BR"))]
           (str title " | " human-date))))))))

(defnc content []
  (hd/div
   {:class "px-4 py-6 whitespace-pre-line border-black
            xl:border-l-2"}
   "Greetings!\n\nI'm a brazilian 🇧🇷 Software Engineer, presently pursuing a "
   (hd/a {:target "_blank"
          :href "https://vtp.ifsp.edu.br/"} "Bachelor's degree")
   " and working on "
   (hd/a {:target "_blank"
          :href "https://moclojer.com"} "Moclojer.")
   " My primary interests include:"
   ($ interests {:left ["Functional Programming"
                        "Distributed Systems"
                        "Multimedia Processing"]
                 :right ["Clojure, ClojureScript"
                         "AWS, GCP, DevOps"
                         "FFmpeg, LibAV"]})
   "My latest articles:"
   ($ latest-articles-cmp {:qtt 2})
   (hd/button
    {:class "mt-8 link font-playfair text-md
             md:hidden"
     :on-click #(refx/dispatch-sync [:toggle-navbar true])}
    "All my articles ->")))

(defnc about []
  (hd/div
   {:class "w-full h-full xl:border-black
            lg:pt-6
            xl:border-b-2"}
   ($ header)
   ($ social-links {:socials [["https://github.com/j0suetm" "Github"]
                              ["https://twitter.com/j0suetm" "Twitter/X"]
                              ["https://linkedin.com/in/josue-teodoro-moreira"
                               "LinkedIn"]]})
   ($ content)))
