(ns com.j0suetm.components.hero
  (:require
   [helix.core :refer [defnc]]
   [helix.dom :as hd]))

(defnc hero []
  (hd/div
   {:class "w-full h-40 border-b-2 border-black
            lg:h-screen lg:w-2/3 lg:border-b-0 lg:border-l-2
            xl:w-[32rem] xl:h-[38rem] xl:my-auto xl:mt-6 xl:border-2 xl:shadow-[0_100px_100px_-100px_rgba(3,4,4,1.0)]"}
   (hd/img
    {:class "w-full h-full object-cover object-[0,-100px]
             md:object-[0,-300px]
             lg:object-[-100px,0]
             xl:object-[0px,0px]"
     :src "resources/assets/img/hero.png"})))
