/**
 * <p>This package defines the interface to the Saxon tree structure. This
 * structure is used to represent both the source document and the stylesheet.
 * Essentially, this class represents Saxon's realization of the XPath data model.</p>
 * <p>The classes in the package are rather a miscellany. What they have in common is that
 * they describe the way the Saxon tree structure is accessed, in a way that it independent
 * of the two tree implementations (in packages <code>net.sf.saxon.tree</code> and
 * <code>net.sf.saxon.tinytree</code>).</p>
 * <p>Broadly speaking, the classes fall into four categories:</p>
 * <ul>
 * <li>Interface classes: DocumentInfo, NodeInfo, Item, ValueRepresentation. These describe the interface
 * offered by the object model to the rest of the system, including the application.
 * </li>
 * <li>Iterator classes: SequenceIterator, AxisIterator, ArrayIterator, EmptyIterator, SingletonIterator, and others.
 * These classes provide mechanisms for iterating over sequences. The most general, and the one which
 * applications are most likely to use, is the SequenceIterator interface itself. AxisIterator is a
 * specialization of this interface whose main difference is that it cannot throw exceptions. The other
 * classes are implementations of SequenceIterator for use in particular circumstances.
 * </li>
 * <li>Shared implementation classes: DocumentPool, NamePool, Navigator, XMLChar.
 * These contain functionality that is shared between the various tree implementations. (However, there is
 * also some shared functionality in the <code>net.sf.saxon.tree</code> package). These classes are
 * not generally needed by applications, with the exception of NamePool, which complex applications may
 * need to manipulate.</li>
 * <li>Information classes: Axis, NamespaceConstant. These contain constants.</li>
 * </ul>
 */
package net.sf.saxon.om;
