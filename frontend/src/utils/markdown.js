import MarkdownIt from 'markdown-it'

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})

export const renderMarkdown = (content) => {
  if (!content) return ''
  return md.render(content)
}

export default md
