package edin.ccg.representation.hockendeps

import edin.ccg.representation.predarg.DepLink
import edin.ccg.representation.tree.{BinaryNode, TerminalNode, TreeNode, UnaryNode}
import edin.ccg.representation.hockendeps.{CCGcat => HockenCat}
import edin.ccg.representation.combinators.Glue

class HockenState(val hockenCat:Option[HockenCat], node:TreeNode){

  lazy val nodeOnlyDeps: List[DepLink] = HockenCatHelper.dependencies(hockenCat)

  def extractDependencies: List[DepLink] = node.allNodes.map(_.hockenState).flatMap(_.nodeOnlyDeps)

}

object HockenState{

  def forNode(node:TreeNode) : HockenState = {
    val hockenCat = node match {
      case node@TerminalNode(word, cat) =>
        HockenCatHelper.lexicalEntry(word, cat, node.position)
      case BinaryNode(Glue(), _, _) =>
        None
      case BinaryNode(comb, left, right) =>
        HockenCatHelper.binaryCombineWithAdjunctionBackoff(comb, left, right)
      case UnaryNode(comb, child) =>
        HockenCatHelper.unaryCombine(comb, child)
    }
    new HockenState(hockenCat, node)
  }

}
