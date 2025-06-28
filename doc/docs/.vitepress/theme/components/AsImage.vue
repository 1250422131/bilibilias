<template>
  <img
    ref="img"
    :src="src"
    :height="height"
    :width="width"
    :alt="alt"
  />
</template>

<script>
import mediumZoom from "medium-zoom";

export default {
  name: "AsImage",
  props: {
    alt: {
      type: String,
      default: "Image",
    },
    src: {
      type: String,
      default: "",
    },
    height: {
      type: String,
      default: "auto",
    },
    width: {
      type: String,
      default: "auto",
    },
    isAmplify: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      isZoomed: false,
      zoomInstance: null,
    };
  },
  mounted() {
    if (this.isAmplify) {
      this.zoomInstance = mediumZoom(this.$refs.img, {
        background: "rgba(0, 0, 0, 0.8)",
        scrollOffset: 50,
      });

      this.zoomInstance.on("open", () => {
        console.log("Zoom opened");
        this.isZoomed = true;
      });
      this.zoomInstance.on("close", () => {
        console.log("Zoom closed");
        this.isZoomed = false;
      });
    }
  },
};
</script>
